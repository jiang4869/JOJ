package cn.jxj4869.joj.controller.admin;

import cn.jxj4869.joj.entity.Blog;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.service.IBlogCommentService;
import cn.jxj4869.joj.service.IBlogService;
import cn.jxj4869.joj.utils.Constants;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-30 15:18
 */
@Controller
@RequestMapping("/admin/blog")
@RequiresRoles("admin")
public class AdminBlogController {


    @Autowired
    private IBlogService blogService;


    @RequestMapping({"", "/list/{page}"})
    public String blogList(@PathVariable(value = "page",required = false) Integer page, Model model) {
        if (ObjectUtils.isEmpty(page)) {
            page = Integer.valueOf(1);
        }

        MyPage<Blog> myPage = new MyPage<>(page, Constants.DEFAULT_ADMIN_PAGE_SIZE);
        MyPage<Blog> blogMyPage = blogService.page(myPage);
        blogMyPage.setShowBtnNum();

        List<Blog> blogList = blogMyPage.getRecords();
        model.addAttribute("blogList", blogList);
        model.addAttribute("page", blogMyPage);
        return "admin/blogList";
    }


    @GetMapping("/{bid}")
    public String blog(@PathVariable("bid")Serializable bid,Model model){
        Blog blog = blogService.getById(bid);
        model.addAttribute("blog",blog);
        return "admin/blogContent";
    }

    @DeleteMapping("/{bid}")
    @ResponseBody
    @Transactional
    public Info blogDelete(@PathVariable("bid") Serializable bid) throws Exception {
        boolean flag = blogService.removeById(bid);
        Info info = new Info();
        if (!flag) {
            info.error("fail");
            throw new Exception("fail");
        } else {
            info.ok("success");
        }
        return info;

    }


}
