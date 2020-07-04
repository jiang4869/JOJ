package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.mapper.SubmissionMapper;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.pojo.vo.StatusVo;
import cn.jxj4869.joj.service.ISubmissionService;
import cn.jxj4869.joj.utils.Constants;
import cn.jxj4869.joj.utils.Tools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Service
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission> implements ISubmissionService {

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Info submissionSave(Submission submission,String originOJ) throws Exception {
        Info info = new Info();

        int count = submissionMapper.insert(submission);
        if(count==1){
            info.ok("submit success");
            info.put("sid",submission.getSid());
            rabbitTemplate.convertAndSend("joj.judger","judger."+originOJ.toLowerCase(),submission);
        }else{
            info.error("submit fail,try again!");
            throw new Exception("submit fail");
        }

        return info;
    }


    @Override
    public MyPage<Submission> submissionListPage(StatusVo statusVo) {
        MyPage<Submission> page=new MyPage<>(statusVo.getPage(), Constants.DEFAULT_Submission_PAGE_SIZE);
        QueryWrapper<Submission> wrapper=new QueryWrapper<>();
        /*
            private String title; // 题目名
            private String OJ; //OJ名
            private String ProbId; // 题目在原OJ的id
            private Integer page=1; //当前页面
            private String userName;  //用户名
         */

        statusVo.setOJ(Tools.trim(statusVo.getOJ()));
        statusVo.setProbId(Tools.trim(statusVo.getProbId()));
        statusVo.setUserName(Tools.trim(statusVo.getUserName()));

        wrapper.like(!StringUtils.isEmpty(statusVo.getOJ())&&!statusVo.getOJ().equalsIgnoreCase("all"),"joj_problem.originOJ",statusVo.getOJ())
                .like(!StringUtils.isEmpty(statusVo.getProbId()),"joj_problem.originProb",statusVo.getProbId())
                .like(!StringUtils.isEmpty(statusVo.getUserName()),"joj_user.userName",statusVo.getUserName())
                .isNull("contestId").or(i->i.apply("joj_contest.endTime<now()"))
                .orderByDesc("subTime");
        MyPage<Submission> submissionMyPage = submissionMapper.selectPage(page, wrapper);

        List<Submission> submissions = submissionMyPage.getRecords();
        Map<String, Map<String, String>> ojLanguageList = Constants.ojLanguageList;
        for (Submission submission : submissions) {
            Problem problem = submission.getProblem();
            submission.setTrueLanguage(ojLanguageList.get(problem.getOriginOJ()).get(submission.getLanguage()));
            System.out.println(submission);
        }
        submissionMyPage.setRecords(submissions);
        submissionMyPage.setShowBtnNum();
        return submissionMyPage;
    }


    @Override
    public MyPage<Submission> problemLatestSubmitPage(Serializable pid, Serializable uid) {
        MyPage<Submission> page = new MyPage<>(1, Constants.DEFAULT_LATEST_SUBMIT_PAGE_SIZE);
        QueryWrapper<Submission> wrapper=new QueryWrapper<>();
        wrapper.eq("joj_submission.pid",pid).eq("joj_submission.uid",uid).orderByDesc("subTime");
        MyPage<Submission> submissionMyPage = submissionMapper.selectPage(page, wrapper);
        submissionMyPage.setShowBtnNum();

        return submissionMyPage;
    }


    @Override
    public List<LinkedHashMap<Object, Object>> submissionStatistic(Serializable pid) {
        QueryWrapper<Submission> wrapper=new QueryWrapper<>();
        wrapper.eq("pid",pid);
        List<LinkedHashMap<Object, Object>> maps = submissionMapper.submissionStatistic(wrapper);
        for (Map<Object, Object> map : maps) {
            System.out.println(map);
        }
        return maps;

    }
}
