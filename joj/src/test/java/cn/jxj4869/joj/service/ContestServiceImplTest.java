package cn.jxj4869.joj.service;

import cn.jxj4869.joj.pojo.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * @author JiangXiaoju
 * @date 2020-06-30 0:34
 */
@SpringBootTest
public class ContestServiceImplTest {

    @Autowired
    private IContestService contestService;

    @Test
    public void test(){
        Map<String, Pair<Integer, Integer>> stringPairMap = contestService.contestSolvedProblemStatistic(3);
        System.out.println(stringPairMap);
    }

}
