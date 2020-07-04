package cn.jxj4869.joj.controller;


import cn.jxj4869.joj.entity.Blog;
import cn.jxj4869.joj.entity.BlogComment;
import cn.jxj4869.joj.entity.User;
import cn.jxj4869.joj.mapper.BlogMapper;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.service.IBlogCommentService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Controller
@RequestMapping("/blogComment")
public class BlogCommentController {


    @Autowired
    IBlogCommentService commentService;

    @Autowired
    BlogMapper blogMapper;

    @RequestMapping("/list/{blogId}")
    public String commentList(@PathVariable("blogId") Integer blogId, @RequestParam(value = "pageNum", defaultValue = "1") Integer page, Model model) {

        MyPage<BlogComment> myPage = commentService.commentPage(page, blogId);
        model.addAttribute("page", myPage);
        List<BlogComment> comments = myPage.getRecords();
        model.addAttribute("comments", comments);

        return "blog/blogContent::commentList";
    }

    @PostMapping("/")
    @ResponseBody
    @RequiresAuthentication
    public Info post(BlogComment comment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Info info = new Info();
        if (user == null) {
//            处理为登录的情况
            info.error("UnLogin");
            return info;
        }

//        判断是否可评论，防止直接通过url链接提交评论
        Blog blog = blogMapper.selectById(comment.getBlogId());
        if (!blog.getPublished() || !blog.getComment()) {
           info.error("Not commentable");
           return info;
        }

        System.out.println(comment);
        info = commentService.commentSave(comment);
        return info;
    }

    @DeleteMapping("/{bcid}")
    @ResponseBody
    @RequiresAuthentication
    public Info post(@PathVariable("bcid") Integer bcid, HttpSession session) {
        User user = (User) session.getAttribute("user");
        BlogComment byId = commentService.getById(bcid);
        Info info = new Info();
        if (user == null || !user.getUid().equals(byId.getUid())) {
            info.error("delete error");
            return info;
        }
        info = commentService.commentDelete(bcid);

        return info;
    }


}
