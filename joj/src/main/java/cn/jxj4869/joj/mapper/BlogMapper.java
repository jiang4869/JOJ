package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Blog;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface BlogMapper extends BaseMapper<Blog> {
    @Override
    @Select("select * from joj_blog where bid=#{id}")
    @Results(value = {
            @Result(property = "blogId",column = "bid"),
            @Result(property = "uid",column = "uid"),
            @Result(property = "user",column = "uid",one=@One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById"))
    })
    Blog selectById(Serializable id);



    @Select("select * from joj_blog where bid=#{id} for update")
    @Results(value = {
            @Result(property = "blogId",column = "bid"),
            @Result(property = "uid",column = "uid"),
            @Result(property = "user",column = "uid",one=@One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById"))
    })
    public Blog getToViewById(Integer id);


    @Override
    @Select("select * from joj_blog ${ew.customSqlSegment}")
    @Results(value = {
            @Result(property = "blogId",column = "bid"),
            @Result(property = "uid",column = "uid"),
            @Result(property = "user",column = "uid",one=@One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById"))
    })
    <E extends IPage<Blog>> E selectPage(E page, @Param(Constants.WRAPPER) Wrapper<Blog> queryWrapper);
}
