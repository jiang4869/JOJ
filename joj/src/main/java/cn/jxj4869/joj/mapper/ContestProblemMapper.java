package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.ContestProblem;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface ContestProblemMapper extends BaseMapper<ContestProblem> {
/*
   private Integer cpid;

    private Integer pid;

    private Integer cid;

    private String alias;

    private String num;

    @TableField(exist = false)
    private Problem problem;
 */



    @Override
    @Select("select * from joj_contest_problem ${ew.customSqlSegment}")
    @Results(id = "cpmap",value = {
            @Result(property = "cpid",column = "cpid"),
            @Result(property = "pid",column = "pid"),
            @Result(property = "cid",column = "cid"),
            @Result(property = "alias",column = "alias"),
            @Result(property = "num",column = "num"),
            @Result(property = "problem",column = "pid",one = @One(select = "cn.jxj4869.joj.mapper.ProblemMapper.selectById"))
    })
    List<ContestProblem> selectList(@Param(Constants.WRAPPER)Wrapper<ContestProblem> queryWrapper);


    @Override
    @Select("select * from joj_contest_problem ${ew.customSqlSegment}")
    @Results(value = {
            @Result(property = "cpid",column = "cpid"),
            @Result(property = "pid",column = "pid"),
            @Result(property = "cid",column = "cid"),
            @Result(property = "alias",column = "alias"),
            @Result(property = "num",column = "num"),
            @Result(property = "problem",column = "pid",one = @One(select = "cn.jxj4869.joj.mapper.ProblemMapper.selectById"))
    })
    ContestProblem selectOne(@Param(Constants.WRAPPER)Wrapper<ContestProblem> queryWrapper);


    @Select("select * from joj_contest_problem where cid=#{id}")
    @ResultMap("cpmap")
    ContestProblem selectByContestId(Serializable id);

    @Override
    @Select("select * from joj_contest_problem where cid=#{id}")
    @ResultMap("cpmap")
    ContestProblem selectById(Serializable id);
}
