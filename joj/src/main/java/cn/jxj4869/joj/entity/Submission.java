package cn.jxj4869.joj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

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

    @TableField(value = "realRunId",select = false)
    private String realRunId;

    private Integer time;

    private Integer memory;

    @TableField("contestId")
    private Integer contestId;

    @TableField("subTime")
    private Date subTime;

    private String language;

    @TableField("displayLanguage")
    private String displayLanguage;

    @TableField(exist = false)
    private String trueLanguage;

    @TableField("sourceCode")
    private String sourceCode;

    @TableField("length")
    private Integer length;

    @TableField(exist = false)
    private Problem problem;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Integer cpid;

    @TableField(exist = false)
    private ContestProblem contestProblem;

    @TableField(exist = false)
    private Contest contest;


}
