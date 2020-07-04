package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.CalendarContest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface CalendarContestMapper extends BaseMapper<CalendarContest> {


    @Update("truncate table joj_calendar_contest")
    public int truncate();

}
