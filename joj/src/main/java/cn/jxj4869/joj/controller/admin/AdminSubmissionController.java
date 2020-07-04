package cn.jxj4869.joj.controller.admin;

import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.entity.User;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.service.ISubmissionService;
import cn.jxj4869.joj.utils.Constants;
import cn.jxj4869.joj.utils.Tools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-30 16:16
 */
@Controller
@RequestMapping("/admin/submission")
@RequiresRoles("admin")
public class AdminSubmissionController {

    @Autowired
    private ISubmissionService submissionService;

    @RequestMapping({"", "/list/{page}"})
    public String submissionList(@PathVariable(value = "page", required = false) Integer page, Model model) {
        if (ObjectUtils.isEmpty(page)) {
            page = Integer.valueOf(1);
        }
        QueryWrapper<Submission> wrapper=new QueryWrapper<>();
        wrapper.orderByDesc("subTime");
        MyPage<Submission> myPage = new MyPage<>(page, Constants.DEFAULT_ADMIN_PAGE_SIZE);
        MyPage<Submission> submissionMyPage = submissionService.page(myPage,wrapper);
        submissionMyPage.setShowBtnNum();
        List<Submission> submissionList = submissionMyPage.getRecords();


        model.addAttribute("submissionsList", submissionList);
        model.addAttribute("page", submissionMyPage);

        return "admin/submissionList";
    }

    @GetMapping("/{sid}")
    public String viewCode(@PathVariable("sid")Serializable sid,Model model){


        Submission submission = submissionService.getById(sid);

        submission.setTrueLanguage(Constants.ojLanguageList.get(submission.getProblem().getOriginOJ()).get(submission.getLanguage()));
//        submission.setSourceCode(MarkdownUtils.markdownToHtml(submission.getSourceCode()));
        model.addAttribute("submission", submission);
        model.addAttribute("prismJS", Tools.findClassPrismJS(submission.getTrueLanguage()));
        return "admin/viewCode";
    }


}
