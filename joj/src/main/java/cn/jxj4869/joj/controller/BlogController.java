package cn.jxj4869.joj.controller;


import cn.jxj4869.joj.entity.Blog;
import cn.jxj4869.joj.entity.BlogType;
import cn.jxj4869.joj.entity.User;
import cn.jxj4869.joj.mapper.BlogMapper;
import cn.jxj4869.joj.mapper.BlogTypeMapper;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.pojo.vo.BlogVo;
import cn.jxj4869.joj.service.IBlogCommentService;
import cn.jxj4869.joj.service.IBlogService;
import cn.jxj4869.joj.service.IBlogTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.events.Comment;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Controller
@RequestMapping("/blog")
public class BlogController {


    @Autowired
    private IBlogTypeService blogTypeService;


    @Autowired
    private IBlogService blogService;

    @Autowired
    private IBlogCommentService commentService;




    @RequestMapping({"list", "list/{page}",""})
    public String list(BlogVo blogVo, Model model, HttpSession session) {
        if (blogVo.getType().equals("self")) {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "blog/unlogin";
            } else {
                blogVo.setUid(user.getUid());
            }

        }
        model.addAttribute("type", blogVo.getType());
        model.addAttribute("blogVo", blogVo);
        MyPage<Blog> blogMyPage = blogService.blogListPage(blogVo);
        List<Blog> blogList = blogMyPage.getRecords();
        model.addAttribute("blogList", blogList);
        model.addAttribute("page", blogMyPage);
        System.out.println(blogVo);
        return "blog/blogList";
    }


    /**
     * 新建博客或者修改博客
     * 登录状态才可以
     *
     * @param model
     * @return
     */
    @GetMapping({"/edit/{id}", "/edit"})
    @RequiresAuthentication
    public String editBlog(@PathVariable(value = "id", required = false) Integer id, Model model, HttpSession session,HttpServletResponse response) {
        Blog blog = blogService.getById(id);

        User user = (User) session.getAttribute("user");

        if(id!=null && ObjectUtils.isEmpty(blog)){
            response.setStatus(404);
            return "error/404";
        }

        if (id != null && !blog.getUid().equals(user.getUid())) {
            return "blog/unlogin";
        }
        if (blog == null) {
            blog = new Blog();
        }
        System.out.println(blog);
        QueryWrapper<BlogType> wrapper = new QueryWrapper<>();
        List<BlogType> blogTypes = blogTypeService.list(wrapper);

        model.addAttribute("blog", blog);
        model.addAttribute("blogTypes", blogTypes);
        return "blog/blogEdit";
    }


    /**
     * 查询博客
     * 若不存在返回404
     * @param blogId
     * @return
     */
    @GetMapping("/{blogId}")
    public String getBlog(@PathVariable("blogId")Integer blogId,Model model,HttpServletResponse response,HttpSession session){

        Blog blog = blogService.getToViewById(blogId);

        User user = (User) session.getAttribute("user");
        if ((blog == null) ||( blog.getPublished() == false && (user == null || !user.getUserName().equals(blog.getUid())))) {
            response.setStatus(404);
            return "error/404";
        }

        model.addAttribute("blog",blog);
        return "blog/blogContent";
    }

    /**
     * 新增博客
     * @param blog
     * @return
     */
    @PostMapping("/")
    @ResponseBody
    @RequiresAuthentication
    public Info postBlog(Blog blog,HttpSession session){
        User user =(User) session.getAttribute("user");
        blog.setUid(user.getUid());
        Info info = blogService.blogSave(blog);
        return info;
    }

    /**
     * 修改博客
     * @param blog
     * @return
     */
    @PutMapping("/{blogId}")
    @ResponseBody
    @RequiresAuthentication
    public Info putBlog(@RequestBody Blog blog){
        System.out.println(blog);

        return blogService.blogUpdate(blog);
    }

    @DeleteMapping("/{blogId}")
    @ResponseBody
    @RequiresAuthentication
    public Info deleteBlog(@PathVariable("blogId") Integer blogId){
        Info info = blogService.blogDelete(blogId);
        return info;
    }
}
