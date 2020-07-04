package cn.jxj4869.joj.controller.admin;

import cn.jxj4869.joj.entity.BlogComment;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.service.IBlogCommentService;
import cn.jxj4869.joj.utils.Constants;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-30 15:39
 */
@Controller
@RequestMapping("/admin/blogComment")
@RequiresRoles("admin")
public class AdminBlogCommentController {

    @Autowired
    private IBlogCommentService blogCommentService;


    @RequestMapping({"", "/list/{page}"})
    public String blogCommentList(@PathVariable(value = "page", required = false) Integer page, Model model) {

        if (ObjectUtils.isEmpty(page)) {
            page = Integer.valueOf(1);
        }

        MyPage<BlogComment> myPage = new MyPage<>(page, Constants.DEFAULT_ADMIN_PAGE_SIZE);
        MyPage<BlogComment> blogCommentMyPage = blogCommentService.page(myPage);
        List<BlogComment> blogCommentList = blogCommentMyPage.getRecords();
        blogCommentMyPage.setShowBtnNum();
        model.addAttribute("blogCommentList", blogCommentList);
        model.addAttribute("page", blogCommentMyPage);


        return "admin/blogCommentList";
    }


    @DeleteMapping("/{bcid}")
    @ResponseBody
    @Transactional
    public Info blogCommentDelete(@PathVariable("bcid")Serializable bcid) throws Exception {
        boolean flag = blogCommentService.removeById(bcid);
        Info info = new Info();
        if(!flag){
            info.error("fail");
            throw new Exception("fail");
        }else{
            info.ok("success");
        }

        return info;

    }
}
