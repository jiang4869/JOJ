package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.ProblemTag;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface ProblemTagMapper extends BaseMapper<ProblemTag> {
/*
    private Integer id;

    @TableField("tagName")
    private String tagName;
 */
    @Select("select joj_problem_tag.* from joj_problem_tag,joj_problem_tags where joj_problem_tag.id=joj_problem_tags.id and joj_problem_tags.pid=#{id}")
    @Results({
            @Result(property = "tagName",column = "tagName")
    })
    public ProblemTag selectByPid(Integer id);

}
