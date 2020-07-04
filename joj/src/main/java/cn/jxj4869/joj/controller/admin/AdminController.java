package cn.jxj4869.joj.controller.admin;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author JiangXiaoju
 * @date 2020-06-30 14:34
 */

@Controller
@RequestMapping("/admin")
@RequiresRoles("admin")
public class AdminController {

    @RequestMapping({""})
    public String index(){
        return "admin/index";
    }
}
