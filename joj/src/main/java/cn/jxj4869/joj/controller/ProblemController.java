package cn.jxj4869.joj.controller;


import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.entity.ProblemTag;
import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.entity.User;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.pojo.vo.ProblemVo;
import cn.jxj4869.joj.service.IProblemService;
import cn.jxj4869.joj.service.IProblemTagService;
import cn.jxj4869.joj.service.ISubmissionService;
import cn.jxj4869.joj.utils.Constants;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private IProblemService problemService;

    @Autowired
    private IProblemTagService problemTagService;

    @Autowired
    private ISubmissionService submissionService;


    /*
    1. page  分页对象
    2. problemList  题目列表
    3. problemVo  查询条件对象
    4. tags 标签列表
    5. OJ列表 所有的oj名

    题目的准确率即Ratio ，暂时不统计，等写了提交的SubmissionController之后再补充。
    题目分类，（所有，未完成，通过）也等提交代码部分写完再完善。
     */

    @GetMapping({"/list/{page}", "/", "", "/list/"})
    public String list(ProblemVo vo, Model model) {

        MyPage<Problem> problemMyPage = problemService.problemListPage(vo);
        List<Problem> problemList = problemMyPage.getRecords();
        model.addAttribute("problemList", problemList);
        model.addAttribute("page", problemMyPage);
        model.addAttribute("problemVo", vo);
        model.addAttribute("OJList", Constants.OJList);
        List<ProblemTag> list = problemTagService.list();
        model.addAttribute("tagList", list);
        return "problem/problemList";
    }


    @GetMapping("/{pid}")
    public String problem(@PathVariable("pid") Integer pid, Model model, HttpSession session) throws JsonProcessingException {
        Problem problem = problemService.selectById(pid);

        /*
            如果用户处于登录状态，则查询最近提交记录
         */
        User user = (User) session.getAttribute("user");
        if (user != null) {
            MyPage<Submission> submissionMyPage = submissionService.problemLatestSubmitPage(pid, user.getUid());
            model.addAttribute("latestSubmit", submissionMyPage.getRecords());
        }

        problem.setIOFormat(Constants.lf.get(problem.getOriginOJ()));
        model.addAttribute("problem", problem);
        model.addAttribute("description", problem.getDescription());
        model.addAttribute("tags", problem.getTags());
        model.addAttribute("aceLanguage", Constants.aceOjLanguageList.get(problem.getOriginOJ()));
        model.addAttribute("ojLanguage", Constants.ojLanguageList.get(problem.getOriginOJ()));
        System.out.println(problem.getDescription());
        return "problem/problemContent";
    }


    @RequestMapping("/findProblem")
    @ResponseBody
    public Info findProblem(String originOJ, String probNum) {
        System.out.println("findProblem");
        System.out.println(originOJ);
        System.out.println(probNum);

        QueryWrapper<Problem> wrapper = new QueryWrapper<>();
        wrapper.eq("originOJ", originOJ).eq("originProb", probNum);
        Problem problem = problemService.getOne(wrapper);
        Info info = new Info();
        if (ObjectUtils.isEmpty(problem)) {
            info.error("No such problem!");
            // 如果问题不存在，交给爬虫机器人去爬取。
            Problem p = new Problem();
            p.setOriginOJ(originOJ);
            p.setOriginProb(probNum);
            problemService.spiderProblem(p);
        } else {
            info.ok("success");
            info.put("data", problem);
        }

        return info;
    }

}
