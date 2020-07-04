package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.pojo.vo.ProblemVo;
import cn.jxj4869.joj.service.IProblemService;
import cn.jxj4869.joj.utils.Constants;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-14 14:16
 */
@SpringBootTest
public class ProblemMapperTest {

    @Autowired
    IProblemService problemService;


    @Autowired
    ProblemMapper problemMapper;

    @Test
    public void problemListPageTest(){
        ProblemVo vo=new ProblemVo();
        List<Integer> list=new ArrayList<>();


        MyPage<Problem> page=new MyPage<>(vo.getPage(), Constants.DEFAULT_PAGE_SIZE);
        QueryWrapper<Problem> wrapper=new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(vo.getTitle()),"title",vo.getTitle())
                .eq(!StringUtils.isEmpty(vo.getOj()),"originOJ",vo.getOj())
                .eq(!StringUtils.isEmpty(vo.getProbId()),"originProb",vo.getProbId())

                .in(!StringUtils.isEmpty(vo.getTags()),"ptags.id",vo.getTags());
        MyPage<Problem> problemMyPage = problemMapper.selectPage(page,wrapper);
        List<Problem> problems = problemMyPage.getRecords();
        for (Problem problem : problems) {
            System.out.println(problem);
        }
    }


    @Test
    public void selectByIdTest(){
        Problem problem = problemMapper.selectById(10);
        System.out.println(problem);
    }

    @Test
    public void selectSolvedProblemListTest(){
        List<Problem> problems = problemMapper.selectSolvedProblemList(3);
        for (Problem problem : problems) {
            System.out.println(problem);
        }

    }
}
