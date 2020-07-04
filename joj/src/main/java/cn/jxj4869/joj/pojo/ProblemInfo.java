package cn.jxj4869.joj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author JiangXiaoju
 * @date 2020-06-30 23:25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProblemInfo {
    private String faShi;  //每到题的罚时时长，分钟为单位
    private Integer faShiNum; // AC前包括AC那次的提交次数
    private Boolean ac; //是否ac
}
