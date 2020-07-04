package cn.jxj4869.joj.service;

import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.pojo.vo.StatusVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface ISubmissionService extends IService<Submission> {

    public Info submissionSave(Submission submission,String originOJ) throws Exception;


    public MyPage<Submission> submissionListPage(StatusVo statusVo);

    public MyPage<Submission> problemLatestSubmitPage(Serializable pid,Serializable uid);

    List<LinkedHashMap<Object, Object>> submissionStatistic(Serializable pid);
}
