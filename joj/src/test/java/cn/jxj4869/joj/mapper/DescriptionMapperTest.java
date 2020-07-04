package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Description;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author JiangXiaoju
 * @date 2020-06-14 16:25
 */
@SpringBootTest
public class DescriptionMapperTest {

    @Autowired
    DescriptionMapper mapper;

    @Test
    public void test(){
        Description description = mapper.selectByPid(10);
        System.out.println(description);
    }
}
