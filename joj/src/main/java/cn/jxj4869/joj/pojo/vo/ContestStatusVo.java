package cn.jxj4869.joj.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JiangXiaoju
 * @date 2020-06-25 1:44
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContestStatusVo {
    private Integer cid;
    private Boolean isSelf=false;
    private Integer page=1;
    private Integer uid;
}
