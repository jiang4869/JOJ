package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Contest;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface ContestMapper extends BaseMapper<Contest> {
/*
    private Integer cid;
    private Integer uid;
    private String title;
    private String description;
    private String announcement;
    private String password;
    private Date beginTime;
    private Date endTime;
    private Integer auth;
    private Integer type;
    private Integer status;
    private String problemList;
    private User user;
 */

    @Override
    @Select("select * from joj_contest where cid=#{id}")
    @Results({
            @Result(property = "cid",column = "cid"),
            @Result(property = "uid",column = "uid"),
            @Result(property = "title",column = "title"),
            @Result(property = "beginTime",column = "beginTime"),
            @Result(property = "endTime",column = "endTime"),
            @Result(property = "auth",column = "auth"),
            @Result(property = "type",column = "type"),
            @Result(property = "status",column = "status"),
            @Result(property = "user",column = "uid",one = @One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById"))
    })
    Contest selectById(Serializable id);

    @Override
    @Select("select * from joj_contest ${ew.customSqlSegment}")
    @Results({
            @Result(property = "cid",column = "cid"),
            @Result(property = "uid",column = "uid"),
            @Result(property = "title",column = "title"),
            @Result(property = "beginTime",column = "beginTime"),
            @Result(property = "endTime",column = "endTime"),
            @Result(property = "auth",column = "auth"),
            @Result(property = "type",column = "type"),
            @Result(property = "status",column = "status"),
            @Result(property = "user",column = "uid",one = @One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById"))
    })
    <E extends IPage<Contest>> E selectPage(E page, @Param(Constants.WRAPPER) Wrapper<Contest> queryWrapper);
}
