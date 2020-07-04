package cn.jxj4869.joj.controller;


import cn.jxj4869.joj.entity.*;
import cn.jxj4869.joj.mapper.BlogCommentMapper;
import cn.jxj4869.joj.mapper.UserMapper;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.service.IBlogCommentService;
import cn.jxj4869.joj.service.IBlogService;
import cn.jxj4869.joj.service.IProblemService;
import cn.jxj4869.joj.service.ISubmissionService;
import cn.jxj4869.joj.utils.Constants;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/console")
public class ConsoleController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogCommentMapper commentMapper;

    @Autowired
    private IBlogCommentService commentService;

    @Autowired
    private IBlogService blogService;


    @Autowired
    private IProblemService problemService;

    /**
     * 用户个人中心首页，任何人都可以访问
     *
     * @param userName
     * @param model
     * @return
     */
    @RequestMapping("{userName}")
    public String home(@PathVariable("userName") String userName, Model model) {
//        查找用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userName", userName);
        User user = userMapper.selectOne(wrapper);
        model.addAttribute("user", user);

//        获取最新博客列表 默认最新十篇博客
        MyPage<Blog> blogMyPage = blogService.blogListPage(user.getUid(), Constants.DEFAULT_PAGE_SIZE, 1);
        List<Blog> blogList = blogMyPage.getRecords();
        model.addAttribute("blogList", blogList);

//        获取最新评论
        List<BlogComment> comments = commentMapper.selectCurrentCommentByUid(user.getUid());
        model.addAttribute("comments", comments);

        // 查找已解决的问题

        List<Problem> list = problemService.selectSolvedProblemList(user.getUid());
        model.addAttribute("solvedList",list);


        return "console/console";
    }


    /**
     * 个人博客管理界面，可以管理所有博客不管是否发布
     * 改请求地址只能登录状态下，用户查看自己的信息，而不能查看其他人的
     * @param userName
     * @param page
     * @param model
     * @param session
     * @return
     */
    @RequestMapping({"/{userName}/blog/{page}", "/{userName}/blog"})
    @RequiresAuthentication
    public String blog(@PathVariable("userName") String userName, @PathVariable(value = "page", required = false) Integer page, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (!user.getUserName().equals(userName)) {
            return "console/unlogin";
        }


        if (page == null) {
            page = 1;
        }

        User user1 = userMapper.selectById(user.getUid());
        model.addAttribute("user", user1);

        //        获取博客列表
        MyPage<Blog> blogMyPage = blogService.blogAllPage(user.getUid(), 10, page);
        List<Blog> blogList = blogMyPage.getRecords();
        model.addAttribute("blogList", blogList);

        model.addAttribute("page", blogMyPage);

        return "console/consoleBlog";
    }


    /**
     * 只有登录状态下的用户才能查看自己的个人信息界面
     *
     * @param userName
     * @return
     */
    @RequestMapping("/{userName}/setting")
    @RequiresAuthentication
    public String setting(@PathVariable("userName") String userName, Model model, HttpSession session) {
        //        查找用户  废除
//        直接从session中获取

        User user = (User) session.getAttribute("user");


        if (!user.getUserName().equals(userName)) {
            return "console/unlogin";
        }

        User user1 = userMapper.selectById(user.getUid());
        model.addAttribute("user", user1);
        return "console/consoleSetting";
    }

}
