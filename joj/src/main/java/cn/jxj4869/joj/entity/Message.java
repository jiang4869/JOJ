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
@TableName("joj_message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "mid", type = IdType.AUTO)
    private Integer mid;

    @TableField("replyMessageId")
    private Integer replyMessageId;

    @TableField("parentMessageId")
    private Integer parentMessageId;

    private Integer uid;

    @TableField("createTime")
    private Date createTime;

    private String content;

    private String email;

    @TableField("nickName")
    private String nickName;

    private Boolean remind;

    private String avatar;



    @TableField(exist = false)
    List<Message> replyMessages;

    @TableField(exist = false)
    private Message replyToMessage; //回复哪一条留言

}
