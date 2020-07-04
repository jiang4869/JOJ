package cn.jxj4869.joj.controller.admin;

import cn.jxj4869.joj.entity.Blog;
import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.service.IProblemService;
import cn.jxj4869.joj.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.org.apache.xpath.internal.operations.Mod;
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
 * @date 2020-06-30 15:51
 */
@Controller
@RequestMapping("/admin/problem")
@RequiresRoles("admin")
public class AdminProblemController {

    @Autowired
    private IProblemService problemService;

    @RequestMapping({"","/list/{page}"})
    public String problemList(@PathVariable(value = "page",required = false) Integer page, Model model){

        if (ObjectUtils.isEmpty(page)) {
            page = Integer.valueOf(1);
        }

        MyPage<Problem> myPage = new MyPage<>(page, Constants.DEFAULT_ADMIN_PAGE_SIZE);

        MyPage<Problem> problemMyPage = problemService.page(myPage);
        List<Problem> problemList = problemMyPage.getRecords();
        problemMyPage.setShowBtnNum();

        model.addAttribute("problemList", problemList);
        model.addAttribute("page", problemMyPage);
        return "admin/problemList";
    }


    @GetMapping("/{pid}")
    public String problem(@PathVariable("pid") Integer pid,Model model) throws JsonProcessingException {
        Problem problem = problemService.selectById(pid);

        problem.setIOFormat(Constants.lf.get(problem.getOriginOJ()));
        model.addAttribute("problem", problem);
        model.addAttribute("description", problem.getDescription());
        model.addAttribute("tags", problem.getTags());
        return "admin/problemContent";
    }


    @DeleteMapping("/{pid}")
    @ResponseBody
    @Transactional
    public Info blogDelete(@PathVariable("pid") Serializable pid) throws Exception {
        boolean flag = problemService.removeById(pid);
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
