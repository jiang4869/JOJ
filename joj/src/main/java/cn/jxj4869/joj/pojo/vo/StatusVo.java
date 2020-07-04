package cn.jxj4869.joj.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-14 1:13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StatusVo {
    private String OJ="all"; //OJ名
    private String probId; // 题目在原OJ的id
    private Integer page=1; //当前页面
    private String userName;  //用户名

}
