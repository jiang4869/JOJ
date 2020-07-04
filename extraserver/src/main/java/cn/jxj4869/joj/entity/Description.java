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
@TableName("joj_description")
public class Description implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "did", type = IdType.AUTO)
    private Integer did;


    private Integer uid;

    private Integer pid; //题目在数据库中的编号

    private String description; //题目描述

    private String input; //输入样例描述

    private String output; //输出样例描述

    private String samples; //样例  样子转成json格式保存

    private String hint; //提示信息

    @TableField("updateTime")
    private Date updateTime;  //更新时间

    private String author; //作者，默认为

    private String remarks;  //备注信息


}
