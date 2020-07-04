package cn.jxj4869.joj;

import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.mapper.SubmissionMapper;
import cn.jxj4869.joj.utils.Tools;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @author JiangXiaoju
 * @date 2020-06-14 13:54
 */
@SpringBootTest
public class JojApplicationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;



    @Autowired
    private SubmissionMapper submissionMapper;

    @Test
    public void test(){
        System.out.println();
    }

    @Test
    public void findStatusTypeTest(){
        System.out.println(Tools.findStatusType("Accepted"));
    }


    @Test
    public void judgeTest(){
        Submission submission = submissionMapper.selectById(35);

        rabbitTemplate.convertAndSend("joj.judger","judger.hdu",submission);
    }


    @Test
    public void testReg(){
        String email="1121429190@qq.com";
        email=email.replaceAll("(?<=.{2})[^@]+(?=.{2}@)","******");
        System.out.println(email);
        System.out.println(new Date().getTime());
        // 1593527749966 5552304570991
    }

}
