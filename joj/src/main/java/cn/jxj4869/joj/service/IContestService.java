package cn.jxj4869.joj.service;

import cn.jxj4869.joj.entity.*;
import cn.jxj4869.joj.pojo.*;
import cn.jxj4869.joj.pojo.vo.BlogVo;
import cn.jxj4869.joj.pojo.vo.ContestStatusVo;
import cn.jxj4869.joj.pojo.vo.ContestVo;
import cn.jxj4869.joj.pojo.vo.StatusVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface IContestService extends IService<Contest> {

    public Info contestAdd(Contest contest, List<ChoiceContestProblem> list) throws Exception;

    public Info contestUpdate(Contest contest, List<ChoiceContestProblem> list) throws Exception;

    public Info contestDelete(Integer cid) throws Exception;

    public MyPage<Submission> contestStatusListPage(ContestStatusVo statusVo);

    public MyPage<Submission> contestProblemLatestSubmitPage(Integer cid,String num,Integer uid);

    public MyPage<Contest> contestListPage(ContestVo contestVo);


    public Info checkAuthority(User user, Serializable cid) throws Exception;


    public Info contestLogin(Serializable cid,String password,User user) throws Exception;

    List<LinkedHashMap<Object, Object>> contestSubmissionStatistic(Serializable cid,String num);


    Map<String, Pair<Boolean,Boolean>> contestUserSolvedProblemStatistic(Serializable cid,Serializable uid);


    Map<String, Pair<Integer,Integer>> contestSolvedProblemStatistic(Serializable cid);


    public List<Participant> contestRank(Serializable cid);
}
