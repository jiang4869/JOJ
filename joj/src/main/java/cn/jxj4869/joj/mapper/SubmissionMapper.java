package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Role;
import cn.jxj4869.joj.entity.Submission;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;
import org.omg.PortableInterceptor.SUCCESSFUL;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface SubmissionMapper extends BaseMapper<Submission> {
    /*

     */
//    selectByContestId




    @Override
    @Select("select joj_submission.*,joj_contest_problem.cpid from joj_submission left join joj_contest on joj_submission.contestId = joj_contest.cid left join joj_contest_problem on joj_submission.pid=joj_contest_problem.pid and joj_contest.cid=joj_contest_problem.cid where sid=#{sid}")
    @Results({
            @Result(property = "sid", column = "sid"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "pid", column = "pid"),
            @Result(property = "status", column = "status"),
            @Result(property = "statusType", column = "statusType"),
            @Result(property = "additionalInfo", column = "additionalInfo"),
            @Result(property = "time", column = "time"),
            @Result(property = "memory", column = "memory"),
            @Result(property = "subTime", column = "subTime"),
            @Result(property = "displayLanguage", column = "displayLanguage"),
            @Result(property = "length", column = "length"),
            @Result(property = "contestId", column = "contestId"),
            @Result(property = "cpid", column = "cpid"),
            @Result(property = "user", column = "uid", one = @One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById")),
            @Result(property = "problem", column = "pid", one = @One(select = "cn.jxj4869.joj.mapper.ProblemMapper.selectById")),
            @Result(property = "contestProblem", column = "cpid", one = @One(select = "cn.jxj4869.joj.mapper.ContestProblemMapper.selectById")),
            @Result(property = "contest", column = "contestId", one = @One(select = "cn.jxj4869.joj.mapper.ContestMapper.selectById"))
    })
    Submission selectById(Serializable id);

    @Override
    @Select("select joj_submission.*,joj_contest_problem.cpid from joj_submission left join joj_contest on joj_submission.contestId=joj_contest.cid inner join joj_problem on joj_submission.pid=joj_problem.pid inner join joj_user on joj_user.uid=joj_submission.uid left join joj_contest_problem on joj_submission.pid=joj_contest_problem.pid and joj_contest.cid=joj_contest_problem.cid ${ew.customSqlSegment}")
    @Results({
            @Result(property = "sid", column = "sid"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "pid", column = "pid"),
            @Result(property = "status", column = "status"),
            @Result(property = "statusType", column = "statusType"),
            @Result(property = "additionalInfo", column = "additionalInfo"),
            @Result(property = "time", column = "time"),
            @Result(property = "memory", column = "memory"),
            @Result(property = "subTime", column = "subTime"),
            @Result(property = "displayLanguage", column = "displayLanguage"),
            @Result(property = "length", column = "length"),
            @Result(property = "contestId", column = "contestId"),
            @Result(property = "cpid", column = "cpid"),
            @Result(property = "user", column = "uid", one = @One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById")),
            @Result(property = "problem", column = "pid", one = @One(select = "cn.jxj4869.joj.mapper.ProblemMapper.selectById")),
            @Result(property = "contestProblem", column = "cpid", one = @One(select = "cn.jxj4869.joj.mapper.ContestProblemMapper.selectById"))
    })
    <E extends IPage<Submission>> E selectPage(E page, @Param(Constants.WRAPPER) Wrapper<Submission> queryWrapper);


    @Select("select joj_submission.*,joj_contest_problem.cpid from joj_submission inner join joj_contest on joj_submission.contestId = joj_contest.cid left join joj_contest_problem on joj_submission.pid=joj_contest_problem.pid and joj_contest.cid=joj_contest_problem.cid ${ew.customSqlSegment}")
    @Results({
            @Result(property = "sid", column = "sid"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "pid", column = "pid"),
            @Result(property = "status", column = "status"),
            @Result(property = "statusType", column = "statusType"),
            @Result(property = "additionalInfo", column = "additionalInfo"),
            @Result(property = "time", column = "time"),
            @Result(property = "memory", column = "memory"),
            @Result(property = "subTime", column = "subTime"),
            @Result(property = "displayLanguage", column = "displayLanguage"),
            @Result(property = "length", column = "length"),
            @Result(property = "contestId", column = "contestId"),
            @Result(property = "cpid", column = "cpid"),
            @Result(property = "user", column = "uid", one = @One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById")),
            @Result(property = "problem", column = "pid", one = @One(select = "cn.jxj4869.joj.mapper.ProblemMapper.selectById")),
            @Result(property = "contestProblem", column = "cpid", one = @One(select = "cn.jxj4869.joj.mapper.ContestProblemMapper.selectById"))
    })
    <E extends IPage<Submission>> E selectContestStatusPage(E page, @Param(Constants.WRAPPER) Wrapper<Submission> queryWrapper);




    <E extends IPage<Submission>> E contestProblemLatestSubmitPage(E page, @Param(Constants.WRAPPER) Wrapper<Submission> queryWrapper);


    @Override
    @Select("select joj_submission.*,joj_contest_problem.cpid from joj_submission inner join joj_contest on joj_submission.contestId=joj_contest.cid inner join joj_problem on joj_submission.pid=joj_problem.pid inner join joj_user on joj_user.uid=joj_submission.uid left join joj_contest_problem on joj_submission.pid=joj_contest_problem.pid and joj_contest.cid=joj_contest_problem.cid ${ew.customSqlSegment}")
    @Results({
            @Result(property = "sid", column = "sid"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "pid", column = "pid"),
            @Result(property = "status", column = "status"),
            @Result(property = "statusType", column = "statusType"),
            @Result(property = "additionalInfo", column = "additionalInfo"),
            @Result(property = "time", column = "time"),
            @Result(property = "memory", column = "memory"),
            @Result(property = "subTime", column = "subTime"),
            @Result(property = "displayLanguage", column = "displayLanguage"),
            @Result(property = "length", column = "length"),
            @Result(property = "contestId", column = "contestId"),
            @Result(property = "cpid", column = "cpid"),
            @Result(property = "user", column = "uid", one = @One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById")),
            @Result(property = "problem", column = "pid", one = @One(select = "cn.jxj4869.joj.mapper.ProblemMapper.selectById")),
            @Result(property = "contestProblem", column = "cpid", one = @One(select = "cn.jxj4869.joj.mapper.ContestProblemMapper.selectById"))
    })
    List<Submission> selectList(@Param(Constants.WRAPPER) Wrapper<Submission> queryWrapper);


    //    @Select("select type,count(statusType) count from  joj_statusType left join (select * from joj_submission where pid=#{pid}) submission on submission.statusType=joj_statusType.type  group by statusType,type  order by type")
//    List<LinkedHashMap<Object,Object>> submissionStatistic(Serializable pid);

    @Select("select type name,count(statusType) value from  joj_statusType left join (select * from joj_submission ${ew.customSqlSegment}) submission on submission.statusType=joj_statusType.id  group by statusType,type  order by type")
    List<LinkedHashMap<Object, Object>> submissionStatistic(@Param(Constants.WRAPPER) Wrapper<Submission> queryWrapper);
}
