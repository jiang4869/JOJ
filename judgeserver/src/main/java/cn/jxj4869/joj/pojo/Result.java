package cn.jxj4869.joj.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Result {
    private String status;
    private String language;
    private String time;
    private String memory;
    private String problemId;

}
