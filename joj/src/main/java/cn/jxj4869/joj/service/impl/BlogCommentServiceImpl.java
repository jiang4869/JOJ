package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.BlogComment;
import cn.jxj4869.joj.mapper.BlogCommentMapper;
import cn.jxj4869.joj.pojo.Email;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.service.IBlogCommentService;
import cn.jxj4869.joj.utils.Constants;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements IBlogCommentService {

    @Autowired
    private BlogCommentMapper commentMapper;


    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public MyPage<BlogComment> commentPage(Integer currentPage, Integer blogId) {
        MyPage<BlogComment> page = new MyPage<>(currentPage, Constants.DEFAULT_PAGE_SIZE);
        MyPage<BlogComment> myPage = commentMapper.selectParentCommentNullPage(page, blogId);
        myPage.setShowBtnNum();
        return myPage;
    }


    @Override
    public MyPage<BlogComment> commentAllPage(Integer currentPage) {
        MyPage<BlogComment> page = new MyPage<>(currentPage, Constants.DEFAULT_PAGE_SIZE);
        QueryWrapper<BlogComment> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("createTime");
        MyPage<BlogComment> commentMyPage = commentMapper.selectPage(page, wrapper);
        commentMyPage.setShowBtnNum();
        return commentMyPage;
    }


    @Async
    public void replyRemind(BlogComment comment) {


        String content = "You are on blog《" + comment.getBlogTitle() + "》Has replied";
        Email email = new Email();
        email.setText(content);
        email.setSubject("Reply remind");
        email.setTo(comment.getEmail());
        rabbitTemplate.convertAndSend("joj.email", "email", email);
    }

    @Override
    @Transactional
    public Info commentSave(BlogComment comment) {
        Info info = new Info();
        comment.setCreateTime(new Date());

        if (comment.getParentCommentId().equals(-1)) {
            comment.setParentCommentId(null);
        } else {
            BlogComment parentComment = commentMapper.selectById(comment.getParentCommentId());
            if (parentComment.getRemind()) {
                replyRemind(parentComment);
            }
        }

        if (comment.getReplyCommentId().equals(-1)) {
            comment.setReplyCommentId(null);
        }


        int cnt = commentMapper.insert(comment);

        if (cnt == 1) {
            info.ok("Saved successfully");
        } else {
            info.error("Save failed");
        }
        return info;
    }


    @Override
    @Transactional
    public Info commentDelete(Integer cid) {
        Info info = new Info();
        int cnt = commentMapper.deleteById(cid);

        if (cnt == 1) {
            info.ok("successfully deleted");
        } else {
            info.error("failed to delete");
        }
        return info;
    }
}
