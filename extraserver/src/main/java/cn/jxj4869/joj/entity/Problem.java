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
import org.apache.ibatis.type.JdbcType;

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

    private Integer uid; //用户id

    private String title;  //题目标题

    private String source; //出处

    private String url; //题目url

    @TableField("originOJ")
    private String originOJ;  //原OJ名

    @TableField("originProb")
    private String originProb; //原OJ上的编号

    @TableField("memoryLimit")
    private Integer memoryLimit; //运行时间限制 -1为unknow

    @TableField("timeLimit")
    private Integer timeLimit;

    @TableField(value = "updateTime")
    private Date updateTime;

}
