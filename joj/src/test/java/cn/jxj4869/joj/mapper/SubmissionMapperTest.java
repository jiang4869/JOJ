package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.utils.Constants;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JiangXiaoju
 * @date 2020-06-15 22:49
 */
@SpringBootTest
public class SubmissionMapperTest {

    @Autowired
    private SubmissionMapper submissionMapper;



    @Test
    public void selectPage(){
        MyPage<Submission> page = new MyPage<>(1, Constants.DEFAULT_Submission_PAGE_SIZE);
        QueryWrapper<Submission>    wrapper=new QueryWrapper<>();
        MyPage<Submission> submissionMyPage = submissionMapper.selectPage(page,wrapper);
        List<Submission> records = submissionMyPage.getRecords();
        for (Submission record : records) {
            System.out.println(record);
        }
    }


    @Test
    public void selectById(){
        Submission submission = submissionMapper.selectById(3);
        System.out.println(submission);
    }

    @Test
    public void submissionStatistic(){
        QueryWrapper<Submission> wrapper=new QueryWrapper<>();
        wrapper.eq("pid",4);
        List<LinkedHashMap<Object, Object>> maps = submissionMapper.submissionStatistic(wrapper);
        for (Map<Object, Object> map : maps) {
            System.out.println(map);
        }
    }


    @Test
    public void selectList(){
        QueryWrapper<Submission>  sWrapper=new QueryWrapper<>();
        sWrapper.eq("contestId",3);

        List<Submission> submissionList = submissionMapper.selectList(sWrapper);
        for (Submission submission : submissionList) {
            System.out.println(submission);
        }
    }
}
