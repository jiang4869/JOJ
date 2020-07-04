package cn.jxj4869.joj.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JiangXiaoju
 * @date 2020-06-20 21:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChoiceContestProblem {
    private String oj;
    private String problemNum;
    private String alias;
    private String num;
}
