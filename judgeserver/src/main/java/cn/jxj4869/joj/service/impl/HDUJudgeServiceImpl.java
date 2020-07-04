package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.service.IJudgeService;
import cn.jxj4869.joj.submitter.HDUSubmitter;
import cn.jxj4869.joj.submitter.Submitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.log4j2.Log4j2LoggerImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author JiangXiaoju
 * @date 2020-06-15 20:46
 */

@Service
@Slf4j
public class HDUJudgeServiceImpl implements IJudgeService {

    @Autowired
    @Qualifier("hduSubmitter1")
    private HDUSubmitter hduSubmitter1;

    @Override
    @RabbitListener(queues = {"judger.hdu"})
    public void receive(Submission submission) {
        log.info(submission.toString());
        System.out.println(submission);
        judge(hduSubmitter1,submission);
    }

    @Override
    public void judge(Submitter submitter, Submission submission) {
            submitter.setSubmission(submission);
            submitter.work();
            submitter.wait2SubmitTimeLimit();
    }
}
