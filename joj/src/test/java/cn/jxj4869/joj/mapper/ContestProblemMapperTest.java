package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.ContestProblem;
import cn.jxj4869.joj.service.IContestProblemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author JiangXiaoju
 * @date 2020-06-23 23:46
 */
@SpringBootTest
public class ContestProblemMapperTest {


    @Autowired
    private IContestProblemService contestProblemService;

    @Autowired
    private ContestProblemMapper mapper;

    @Test
    public void test(){
        QueryWrapper<ContestProblem> wrapper=new QueryWrapper<>();
        wrapper.eq("cid",3).eq("num","A");
        ContestProblem cp = contestProblemService.getOne(wrapper);
        System.out.println(cp);
    }

}
