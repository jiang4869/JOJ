package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.BlogComment;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.utils.Constants;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author JiangXiaoju
 * @date 2020-06-14 14:45
 */
@SpringBootTest
public class BlogCommentMapperTest {

    @Autowired
    BlogCommentMapper commentMapper;

    @Test
    public void test(){

        MyPage<BlogComment> page=new MyPage<>(1, Constants.DEFAULT_PAGE_SIZE);
        QueryWrapper<BlogComment> wrapper=new QueryWrapper<>();
        wrapper.orderByDesc("createTime");
        wrapper.eq("uid",3);
        MyPage<BlogComment> commentMyPage = commentMapper.selectPage(page, wrapper);
        commentMyPage.setShowBtnNum();
    }
}
