package cn.jxj4869.joj.pojo;

import cn.jxj4869.joj.entity.ContestProblem;
import cn.jxj4869.joj.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @author JiangXiaoju
 * @date 2020-06-30 22:27
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Participant {
    private User user;
    private Integer solved; //过题数
    private Long time;
    private Long sortTime; //罚时
    private String penalty;
    private Map<String, ProblemInfo> problemDataList;


}
