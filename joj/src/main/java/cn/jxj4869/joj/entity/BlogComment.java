package cn.jxj4869.joj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("joj_blog_comment")
public class BlogComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "bcid", type = IdType.AUTO)
    private Integer bcid;

    @TableField("bid")
    private Integer blogId;

    @TableField("replyCommentId")
    private Integer replyCommentId;

    @TableField("parentCommentId")
    private Integer parentCommentId;

    private Integer uid;

    private String content;

    @TableField("createTime")
    private Date createTime;

    private String email;

    private Boolean remind;



    @TableField(exist = false)
    List<BlogComment> replyComments;

    @TableField(exist = false)
    private BlogComment replyToComment; //回复哪一条评论，若评论本身父评论则为null

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private String blogTitle;

}
