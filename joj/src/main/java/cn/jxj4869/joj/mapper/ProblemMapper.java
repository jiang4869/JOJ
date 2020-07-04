package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Blog;
import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.entity.ProblemTag;
import cn.jxj4869.joj.entity.Submission;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface ProblemMapper extends BaseMapper<Problem> {

/*
    private Integer pid;
    private Integer uid;
    private String title;
    private String source;
    private String url;
    private String originOJ;
    private String originProb;
    private Integer memoryLimit;
    private Integer timeLimit;
    private Date updateTime;
    private List<ProblemTag> tags;

 */
    @Override
    @Select("select joj_problem.* from joj_problem inner join joj_problem_tags ptags on joj_problem.pid=ptags.pid ${ew.customSqlSegment}")
    @Results(value = {
            @Result(property = "originOJ",column = "originOJ"),
            @Result(property = "originProb",column = "originProb"),
            @Result(property = "memoryLimit",column = "memoryLimit"),
            @Result(property = "timeLimit",column = "timeLimit"),
            @Result(property = "updateTime",column = "updateTime"),
            @Result(property = "user",column = "uid",one = @One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById"))
    })
    <E extends IPage<Problem>> E selectPage(E page, @Param(Constants.WRAPPER) Wrapper<Problem> queryWrapper);


    @Override
    @Select("select joj_problem.* from joj_problem inner join joj_problem_tags ptags on joj_problem.pid=ptags.pid where joj_problem.pid=#{pid}")
    @Results(value = {
            @Result(property = "pid",column = "pid"),
            @Result(property = "originOJ",column = "originOJ"),
            @Result(property = "originProb",column = "originProb"),
            @Result(property = "memoryLimit",column = "memoryLimit"),
            @Result(property = "timeLimit",column = "timeLimit"),
            @Result(property = "updateTime",column = "updateTime"),
            @Result(property = "description",column = "pid",one=@One(select = "cn.jxj4869.joj.mapper.DescriptionMapper.selectByPid")),
            @Result(property = "tags",column = "pid",many=@Many(select = "cn.jxj4869.joj.mapper.ProblemTagMapper.selectByPid"))

    })
    Problem selectById(Serializable id);



    @Select("select distinct joj_problem.* from joj_problem inner join joj_submission on joj_problem.pid = joj_submission.pid left join joj_contest on joj_submission.contestId=joj_contest.cid where joj_submission.uid=#{uid} and (joj_submission.contestId is null or joj_contest.endTime<now()) ")
    List<Problem> selectSolvedProblemList(Serializable uid);
}
