package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Description;
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
public interface DescriptionMapper extends BaseMapper<Description> {


    @Select("select * from joj_description where pid=#{id}")
    @Results(value = {
            @Result(property = "updateTime",column = "updateTime")
    })
    Description selectByPid(Integer id);

}
