package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.BlogComment;
import cn.jxj4869.joj.pojo.MyPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;

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
public interface BlogCommentMapper extends BaseMapper<BlogComment> {

    @Override
    @Select("select *,title from joj_blog_comment,joj_blog where bcid=#{id} and joj_blog.bid=joj_blog_comment.bid")
    @Results(value = {
            @Result(property = "uid",column = "uid"),
            @Result(property = "bcid",column = "bcid"),
            @Result(property = "blogId",column = "bid"),
            @Result(property = "blogTitle",column = "title"),
            @Result(property = "user",column = "uid",one=@One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById"))
    })
    BlogComment selectById(Serializable id);

    @Select("select * from joj_blog_comment where parentCommentId is null and bid=#{bid}")
    @Results(value = {
            @Result(property = "uid",column = "uid"),
            @Result(property = "bcid",column = "bcid"),
            @Result(property = "blogId",column = "bid"),
            @Result(property = "user",column = "uid",one=@One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById")),
            @Result(property = "replyComments",column = "bcid",many=@Many(select = "cn.jxj4869.joj.mapper.BlogCommentMapper.selectByParentCommentId"))
    })
    MyPage<BlogComment> selectParentCommentNullPage(MyPage<BlogComment> page,@Param("bid") Integer bid);


    @Select("select * from joj_blog_comment where parentCommentId=#{cid}")
    @Results(value = {
            @Result(property = "uid",column = "uid"),
            @Result(property = "bcid",column = "bcid"),
            @Result(property = "blogId",column = "bid"),
            @Result(property = "replyCommentId",column = "replyCommentId"),
            @Result(property = "user",column = "uid",one=@One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById")),
            @Result(property = "replyToComment",column = "replyCommentId",one=@One(select = "cn.jxj4869.joj.mapper.BlogCommentMapper.selectById"))
    })
    List<BlogComment> selectByParentCommentId(Integer cid);






    @Select("select bc.*,bb.title from joj_blog_comment bc,joj_blog bb where bc.bid=bb.bid and bb.uid=#{uid} order by createTime desc limit 10")
    @Results(value = {
            @Result(property = "uid",column = "uid"),
            @Result(property = "bcid",column = "bcid"),
            @Result(property = "blogId",column = "bid"),
            @Result(property = "user",column = "uid",one=@One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById")),
            @Result(property = "blogTitle",column = "title")
    })
    List<BlogComment> selectCurrentCommentByUid(Integer uid);


    @Override
    @Select("select joj_blog_comment.*,title from joj_blog_comment,joj_blog where joj_blog.bid=joj_blog_comment.bid ${ew.customSqlSegment}")
    @Results(value = {
            @Result(property = "uid",column = "uid"),
            @Result(property = "blogTitle",column = "title"),
            @Result(property = "bcid",column = "bcid"),
            @Result(property = "user",column = "uid",one=@One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById"))
    })
    <E extends IPage<BlogComment>> E selectPage(E page, @Param(Constants.WRAPPER) Wrapper<BlogComment> queryWrapper);
}
