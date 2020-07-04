package cn.jxj4869.joj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("joj_statistics")
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "psid", type = IdType.AUTO)
    private Integer psid;

    private Integer pid;

    @TableField("AC")
    private Integer ac;

    @TableField("PE")
    private Integer pe;

    @TableField("WA")
    private Integer wa;

    @TableField("TLE")
    private Integer tle;

    @TableField("MLE")
    private Integer mle;

    @TableField("OLE")
    private Integer ole;

    @TableField("RE")
    private Integer re;

    @TableField("CE")
    private Integer ce;

    @TableField("UE")
    private Integer ue;


}
