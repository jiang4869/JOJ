package cn.jxj4869.joj.pojo.vo;

import cn.jxj4869.joj.entity.ProblemTag;
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
public class ProblemVo {
    private String type="all"; // all, solved, attempted
    private String title; // 题目名
    private String oj="ALL"; //OJ名
    private String probId; // 题目在原OJ的id
    private Integer page=1; //当前页面
    private Integer uid;  //用户id
    private String tags; // 标签
}
