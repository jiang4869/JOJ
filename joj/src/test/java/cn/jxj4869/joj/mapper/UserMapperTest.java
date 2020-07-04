package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-30 14:53
 */
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("cid",3);
        List<User> users = userMapper.selectParticipateContest(wrapper);
        for (User user : users) {
            System.out.println(user);

        }

    }
}
