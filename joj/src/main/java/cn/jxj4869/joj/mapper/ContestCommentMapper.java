package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.BlogComment;
import cn.jxj4869.joj.entity.ContestComment;
import cn.jxj4869.joj.pojo.MyPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface ContestCommentMapper extends BaseMapper<ContestComment> {

    /*
    private Integer ccid;
    private Integer replyCommentId;
    private Integer uid;
    private Integer parentCommentId;
    private Integer cid;
    private String content;
    private Date createTime;
    List<ContestComment> replyComments;
    private ContestComment replyToComment; //回复哪一条评论，若评论本身父评论则为null
     */

    @Override
    @Select("select * from joj_contest_comment where ccid=#{id}")
    @Results({
            @Result(property = "ccid",column = "ccid"),
            @Result(property = "replyCommentId",column = "replyCommentId"),
            @Result(property = "uid",column = "uid"),
            @Result(property = "parentCommentId",column = "parentCommentId"),
            @Result(property = "cid",column = "cid"),
            @Result(property = "content",column = "content"),
            @Result(property = "createTime",column = "createTime"),
            @Result(property = "user",column = "uid",one = @One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById")),
    })
    ContestComment selectById(Serializable id);

    @Select("select * from joj_contest_comment where parentCommentId is null and cid=#{cid}")
    @Results({
            @Result(property = "ccid",column = "ccid"),
            @Result(property = "uid",column = "uid"),
            @Result(property = "cid",column = "cid"),
            @Result(property = "content",column = "content"),
            @Result(property = "createTime",column = "createTime"),
            @Result(property = "user",column = "uid",one = @One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById")),
            @Result( property = "replyComments",column = "ccid",many=@Many(select = "cn.jxj4869.joj.mapper.ContestCommentMapper.selectByParentCommentId"))
    })
    List<ContestComment> selectParentCommentNull(@Param("cid") Integer cid);

    @Select("select * from joj_contest_comment where parentCommentId=#{pcid}")
    @Results({
            @Result(property = "ccid",column = "ccid"),
            @Result(property = "replyCommentId",column = "replyCommentId"),
            @Result(property = "uid",column = "uid"),
            @Result(property = "cid",column = "cid"),
            @Result(property = "content",column = "content"),
            @Result(property = "createTime",column = "createTime"),
            @Result(property = "user",column = "uid",one = @One(select = "cn.jxj4869.joj.mapper.UserMapper.selectById")),
            @Result( property = "replyToComment",column = "replyCommentId",one=@One(select = "cn.jxj4869.joj.mapper.ContestCommentMapper.selectById"))
    })
    List<ContestComment> selectByParentCommentId(Integer pcid);
}
