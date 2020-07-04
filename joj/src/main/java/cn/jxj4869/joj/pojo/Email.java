package cn.jxj4869.joj.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JiangXiaoju
 * @date 2020-06-12 22:48
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Email {
    private String text;
    private String to;
    private String subject;
}
