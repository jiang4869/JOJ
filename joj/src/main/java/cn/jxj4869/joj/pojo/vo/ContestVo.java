package cn.jxj4869.joj.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JiangXiaoju
 * @date 2020-06-25 17:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContestVo {
    private String title;
    private Integer page=1;
    private Integer type=-1; // all -1 scheduled 0 running 1 end 2
    private Boolean isSelf=false; // false 查看全部，true 查看用户自己
    private Integer uid;
}
