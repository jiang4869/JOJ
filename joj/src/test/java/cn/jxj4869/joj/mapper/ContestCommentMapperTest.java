package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.ContestComment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-25 10:30
 */
@SpringBootTest
public class ContestCommentMapperTest {

    @Autowired
    private ContestCommentMapper commentMapper;

    @Test
    public void test(){
        List<ContestComment> contestComments = commentMapper.selectParentCommentNull(3);
        for (ContestComment contestComment : contestComments) {
            System.out.println(contestComment);
        }
        System.out.println(new Date());
    }
}
