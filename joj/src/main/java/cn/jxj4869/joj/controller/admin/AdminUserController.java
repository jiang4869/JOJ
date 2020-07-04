package cn.jxj4869.joj.controller.admin;

import cn.jxj4869.joj.entity.User;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.service.IUserService;
import cn.jxj4869.joj.utils.Constants;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
 * @date 2020-06-30 14:59
 */

@Controller
@RequestMapping("/admin/user")
@RequiresRoles("admin")
public class AdminUserController {

    @Autowired
    private IUserService userService;

    @RequestMapping({"", "/list/{page}"})
    public String userList(@PathVariable(value = "page", required = false) Integer page, Model model) {
        if (ObjectUtils.isEmpty(page)) {
            page = Integer.valueOf(1);
        }
        MyPage<User> myPage=new MyPage<>(page, Constants.DEFAULT_ADMIN_PAGE_SIZE);
        MyPage<User> userMyPage = userService.page(myPage);
        userMyPage.setShowBtnNum();
        List<User> userList = userMyPage.getRecords();

        System.out.println("======================");
        System.out.println(userMyPage);

        model.addAttribute("userList",userList);
        model.addAttribute("page",userMyPage);

        return "admin/userList";
    }



    @DeleteMapping("/{uid}")
    @ResponseBody
    @Transactional
    public Info userDelete(@PathVariable("uid")Serializable uid) throws Exception {
        boolean flag = userService.removeById(uid);
        Info info = new Info();

        if (!flag){
            info.error("fail");
            throw new Exception("fail");
        }else{
            info.ok("success");
        }
        return info;
    }
}
