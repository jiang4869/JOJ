package cn.jxj4869.joj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
@TableName("joj_contest_problem")
public class ContestProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "cpid", type = IdType.AUTO)
    private Integer cpid;

    private Integer pid;

    private Integer cid;

    private String alias;

    private String num;

    @TableField(exist = false)
    private Problem problem;

}
