package cn.jxj4869.joj.controller;


import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.entity.User;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.service.ISubmissionService;
import cn.jxj4869.joj.service.impl.SubmissionServiceImpl;
import cn.jxj4869.joj.utils.Constants;

import cn.jxj4869.joj.utils.MarkdownUtils;
import cn.jxj4869.joj.utils.Tools;
import org.apache.http.HttpResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Controller
@RequestMapping("/submission")
public class SubmissionController {


    @Autowired
    private ISubmissionService submissionService;

    @Value("${joj.status.pending}")
    private String statusPending;

    @PostMapping("/problem/{pid}")
    @ResponseBody
    @RequiresAuthentication
    public Info problemSubmit(Submission submission, @RequestParam("originOJ") String originOJ, HttpSession session) {

        if (ObjectUtils.isEmpty(submission)) {
            return new Info().error("The submit is empty");
        }
        /*
           通过OJ名获取对应OJ的languageList，然后映射到对应显示的名称。
                1. C++
                2. C
                3. Java
                4. Python
                5. other
         */
        submission.setDisplayLanguage(Tools.findShowLanguage(Constants.ojLanguageList.get(originOJ).get(submission.getLanguage())));
        /*
            每个提交对应的状态 ,1~11为statusType对应的值
            1. AC
            2. PE
            3. WA
            4. TLE
            5. MLE
            6. OLE
            7. RE
            8. CE
            9. UE
            10. Queuing & Judging
            11. Pending  // 提交后，保存到数据库中的状态。
         */
        submission.setStatus(statusPending);
        submission.setStatusType(Tools.findStatusType(statusPending));

        User user = (User) session.getAttribute("user");

        submission.setUid(user.getUid());
        submission.setSubTime(new Date());
        System.out.println(submission);
        submission.setLength(submission.getSourceCode().length());
        Info info = new Info();
        try {
            info = submissionService.submissionSave(submission, originOJ);
            info.ok("submit success");

        } catch (Exception e) {
            info.error("submit fail,try again!");
        }
        return info;
    }

    /**
     * TODO:权限判断  比赛中的代码只有本人和比赛所有者可以查看。其他情况，代码公开
     *
     * @param sid
     * @param model
     * @param response
     * @return
     */
    @GetMapping("/{sid}")
    public Object status(@PathVariable("sid") Integer sid, Model model, HttpServletResponse response, HttpSession session) {

        Submission submission = submissionService.getById(sid);
        if (ObjectUtils.isEmpty(submission)) {
            response.setStatus(404);
            return "error/404";
        }
                            // 比赛中的代码，并且比赛还没结束，并且用户未登录或则（用户不是代码所有者并且不是比赛所有者）
        User user = (User) session.getAttribute("user");
        if (submission.getContestId() != null && submission.getContest().getEndTime().getTime() > System.currentTimeMillis()
                && (ObjectUtils.isEmpty(user) || !submission.getUid().equals(user.getUid()) && !submission.getContest().getUid().equals(user.getUid()))) {

        } else {
            submission.setTrueLanguage(Constants.ojLanguageList.get(submission.getProblem().getOriginOJ()).get(submission.getLanguage()));
//        submission.setSourceCode(MarkdownUtils.markdownToHtml(submission.getSourceCode()));
            model.addAttribute("submission", submission);
            model.addAttribute("prismJS", Tools.findClassPrismJS(submission.getTrueLanguage()));
        }

        return "status/viewCode";
    }


    @GetMapping("/statistic/{pid}")
    @ResponseBody
    public Info statistic(@PathVariable("pid") Serializable pid) {

        Info info = new Info();
        try {
            info.put("data", submissionService.submissionStatistic(pid));
            info.ok("success");
        } catch (Exception e) {
            info.error(e.getMessage());
        }
        return info;
    }

}
