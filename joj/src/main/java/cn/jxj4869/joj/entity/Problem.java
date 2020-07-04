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
@TableName("joj_problem")
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pid", type = IdType.AUTO)
    private Integer pid;

    private Integer uid;

    private String title;

    private String source;

    private String url;

    @TableField("originOJ")
    private String originOJ;

    @TableField("originProb")
    private String originProb;

    @TableField("memoryLimit")
    private Integer memoryLimit;

    @TableField("timeLimit")
    private Integer timeLimit;

    @TableField("updateTime")
    private Date updateTime;


    @TableField(exist = false)
    private List<ProblemTag> tags;


    @TableField(exist = false)
    private Description description;

    @TableField(exist = false)
    private String IOFormat;

    @TableField(exist = false)
    private User user;
}
