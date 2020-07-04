package cn.jxj4869.joj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
@TableName("joj_submission")
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sid", type = IdType.AUTO)
    private Integer sid;

    private Integer uid;

    private Integer pid;

    private String status;

    @TableField("statusType")
    private Integer statusType;

    @TableField(exist = false)
    private String showStatus;


    @TableField("additionalInfo")
    private String additionalInfo;

    @TableField("realRunId")
    private String realRunId;

    private Integer time;

    private Integer memory;

    @TableField("subTime")
    private Date subTime;

    private String language;

    @TableField("displayLanguage")
    private String displayLanguage;

    @TableField(exist = false)
    private String showLanguage;

    @TableField("sourceCode")
    private String sourceCode;

    @TableField("length")
    private Integer length;

}
