package cn.jxj4869.joj.entity;

import cn.jxj4869.joj.pojo.Sample;
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
@TableName("joj_description")
public class Description implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "did", type = IdType.AUTO)
    private Integer did;

    private Integer uid;

    private Integer pid;

    private String description;

    private String input;

    private String output;

    private String samples;

    private String hint;

    @TableField("updateTime")
    private Date updateTime;

    private String author;

    private String remarks;


    @TableField(exist = false)
    private List<Sample> sampleList;

}
