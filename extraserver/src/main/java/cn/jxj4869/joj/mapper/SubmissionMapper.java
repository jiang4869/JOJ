package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Submission;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface SubmissionMapper extends BaseMapper<Submission> {

    @Select("select count(*)  from joj_submission where  date_format(subTime,'%Y-%m-%d')=#{date} ")
    Integer countSubmit(String date);

    @Select("select count(*)  from joj_submission where statusType=1 and date_format(subTime,'%Y-%m-%d')=#{date}")
    Integer countACSubmit(String date);
}
