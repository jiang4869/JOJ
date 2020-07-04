package cn.jxj4869.joj.controller.admin;

import cn.jxj4869.joj.entity.Contest;
import cn.jxj4869.joj.entity.User;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.service.IContestService;
import cn.jxj4869.joj.utils.Constants;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-30 16:41
 */
@Controller
@RequestMapping("/admin/contest")
@RequiresRoles("admin")
public class AdminContestController {

    @Autowired
    private IContestService contestService;

    @RequestMapping({"", "/list/{page}"})
    public String userList(@PathVariable(value = "page", required = false) Integer page, Model model) {
        if (ObjectUtils.isEmpty(page)) {
            page = Integer.valueOf(1);
        }
        MyPage<Contest> myPage=new MyPage<>(page, Constants.DEFAULT_ADMIN_PAGE_SIZE);
        MyPage<Contest> contestMyPage = contestService.page(myPage);
        contestMyPage.setShowBtnNum();
        List<Contest> contestList = contestMyPage.getRecords();



        model.addAttribute("contestList",contestList);
        model.addAttribute("page",contestMyPage);

        return "admin/contestList";
    }

}
