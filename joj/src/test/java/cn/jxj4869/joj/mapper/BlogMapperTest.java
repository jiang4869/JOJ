package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Blog;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.pojo.vo.BlogVo;
import cn.jxj4869.joj.utils.Constants;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-14 14:43
 */
@SpringBootTest
public class BlogMapperTest {
    @Autowired
    BlogMapper blogMapper;

    @Test
    public void test(){


        BlogVo blogVo = new BlogVo();
        MyPage<Blog> page=new MyPage<>(blogVo.getPage(), Constants.DEFAULT_PAGE_SIZE);
        QueryWrapper<Blog> wrapper=new QueryWrapper<>();
        wrapper.like(blogVo.getWord()!=null,"title",blogVo.getWord()).eq(blogVo.getType().equals("self"),"uid",blogVo.getUid()).eq("published",true)
                .orderByDesc(blogVo.getOrderBy().equals("updateTime"),"updateTime").orderByDesc(blogVo.getOrderBy().equals("view"),"views")
                .orderByDesc(blogVo.getOrderBy().equals("default"),"updateTime");
        MyPage<Blog> blogMyPage = blogMapper.selectPage(page, wrapper);
        List<Blog> records = blogMyPage.getRecords();
        for (Blog record : records) {
            System.out.println(record);
        }
    }

}
