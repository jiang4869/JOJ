package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Contest;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface ContestMapper extends BaseMapper<Contest> {


    @Update("update joj_contest\n" +
            "set joj_contest.status=\n" +
            "        case\n" +
            "            when beginTime > now()\n" +
            "                then 0\n" +
            "            when endTime < now()\n" +
            "                then 2\n" +
            "            else 1\n" +
            "            end\n" +
            ";")
    public void updateStatus();
}
