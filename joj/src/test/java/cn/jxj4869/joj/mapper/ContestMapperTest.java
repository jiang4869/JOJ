package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Contest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-25 1:01
 */
@SpringBootTest
public class ContestMapperTest {

    @Autowired
    private ContestMapper contestMapper;


    @Test
    public void test(){
        QueryWrapper<Contest> wrapper=new QueryWrapper<>();
        wrapper.le("beginTime","now()");

        List<Contest> contests = contestMapper.selectList(wrapper);
        for (Contest contest : contests) {
            System.out.println(contest);
        }
    }
}
