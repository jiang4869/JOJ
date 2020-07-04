package cn.jxj4869.joj.submitter;

import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.mapper.ProblemMapper;
import cn.jxj4869.joj.mapper.SubmissionMapper;
import cn.jxj4869.joj.pojo.Result;
import cn.jxj4869.joj.utils.Tools;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author JiangXiaoju
 * @date 2020-06-15 18:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class Submitter {
    protected Submission submission;

    protected String userName;
    protected String password;

    @Autowired
    protected SubmissionMapper submissionMapper;
    @Autowired
    protected ProblemMapper problemMapper;

    protected CloseableHttpClient httpClient = HttpClients.createDefault();
    protected RequestConfig config = RequestConfig.custom().setConnectTimeout(1000)
            .setConnectionRequestTimeout(500)
            .setSocketTimeout(10 * 1000)
            .build();


    public Submitter(String userName, String password) throws Exception {
        this.userName = userName;
        this.password = password;
        login();
    }

    protected abstract void login() throws Exception;

    protected abstract void submit() throws Exception;

    protected abstract void getAns() throws Exception;

    public abstract void wait2SubmitTimeLimit();


    @Transactional
    protected void updateSubmission() {
        submissionMapper.updateById(submission);
    }

    protected abstract void getAdditionalInfo() throws Exception;


    public void work() {
        try {
            try {
                submit();
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(2000);
                // 长时间未提交会自动注销，所以第一次提交失败后，先尝试登录一下。
                login();
                Thread.sleep(2000);
                submit();
            }
            submission.setStatus("Running & Judging");
            submission.setStatusType(Tools.findStatusType("Running & Judging"));
            updateSubmission();
            getAns();
            updateSubmission();
        } catch (Exception e) {
            submission.setStatus("Submit Fail");
            submission.setStatusType(Tools.findStatusType("Submit Fail"));
            updateSubmission();
            e.printStackTrace();
        }finally {
            this.submission = null;
        }


    }


}
