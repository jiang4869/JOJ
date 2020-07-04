package cn.jxj4869.joj.controller;


import cn.jxj4869.joj.entity.*;
import cn.jxj4869.joj.pojo.*;
import cn.jxj4869.joj.pojo.vo.ContestStatusVo;
import cn.jxj4869.joj.pojo.vo.ContestVo;
import cn.jxj4869.joj.service.IContestCommentService;
import cn.jxj4869.joj.service.IContestProblemService;
import cn.jxj4869.joj.service.IContestService;
import cn.jxj4869.joj.service.ISubmissionService;
import cn.jxj4869.joj.utils.Constants;
import cn.jxj4869.joj.utils.MarkdownUtils;
import cn.jxj4869.joj.utils.Tools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 * <p>
 * TODO:时间允许则将 contestInfo部分单独提取出来。代码过于冗余
 *
 * TODO:权限判断部分，之后改成用拦截器或则过滤器。现在代码太过冗余。
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Controller
@RequestMapping("/contest")
public class ContestController {

    @Autowired
    private IContestService contestService;


    @Autowired
    private IContestProblemService contestProblemService;

    @Autowired
    private ISubmissionService submissionService;

    @Autowired
    private IContestCommentService contestCommentService;


    @Value("${joj.status.pending}")
    private String statusPending;

    /**
     * 比赛列表
     * @param contestVo
     * @param model
     * @param session
     * @return
     */
    @GetMapping({"", "/list/{page}"})
    public String index(ContestVo contestVo, Model model, HttpSession session) {

        if (contestVo.getIsSelf()) {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "contest/contestUnlogin";
            } else {
                contestVo.setUid(user.getUid());
            }

        }
        model.addAttribute("type", contestVo.getType());
        model.addAttribute("contestVo", contestVo);
        MyPage<Contest> contestMyPage = contestService.contestListPage(contestVo);
        List<Contest> contestList = contestMyPage.getRecords();
        model.addAttribute("contestList", contestList);
        model.addAttribute("page", contestMyPage);
        System.out.println(contestVo);

        return "contest/contestList";
    }

    /**
     * 添加比赛界面
     * 添加比赛时，只有在线用户才行。
     * 若更新时进行时，只有比赛所有者才可以，要进行判断 cid是否为null。
     * TODO：权限判断尚未完成
     * 2020-06-27 18:19:00 权限设置完成，等待验证
     *
     * @param model
     * @param cid
     * @return
     */
    @GetMapping({"/add", "/add/{cid}"})
    @RequiresAuthentication
    public String add(Model model, @PathVariable(value = "cid", required = false) Integer cid, HttpSession session, HttpServletResponse response) {
        Contest contest;
        if (cid != null) {
            contest = contestService.getById(cid);
            if (ObjectUtils.isEmpty(contest)) {
                response.setStatus(404);
                return "error/404";
            }

            User user = (User) session.getAttribute("user");
            if (!contest.getUid().equals(user.getUid())) {
                response.setStatus(403);
                return "contest/unlogin";
            }

            QueryWrapper<ContestProblem> wrapper = new QueryWrapper<>();
            wrapper.eq("cid", cid);
            List<ContestProblem> list = contestProblemService.list(wrapper);
            model.addAttribute("problemList", list);
        } else {
            contest = new Contest();
        }

        model.addAttribute("contest", contest);
        return "contest/contestAdd";
    }

    /**
     * 添加比赛
     * 把比赛创建者添加到 joj_participate中  暂时完成该功能
     * @param contest
     * @param session
     * @return
     * @throws Exception
     */
    @PostMapping("/")
    @ResponseBody
    @RequiresAuthentication
    public Info addContest(Contest contest, HttpSession session) throws Exception {

//        System.out.println(contest);
        String problemList = contest.getProblemList();
        System.out.println(problemList);

        User user = (User) session.getAttribute("user");
        contest.setUid(user.getUid());

        List<ChoiceContestProblem> list = new ArrayList<>();
        JSONArray objects = new JSONArray(problemList);
        System.out.println(objects.length());
        for (int i = 0; i < objects.length(); i++) {
            Object o = objects.get(i);
            JSONObject jsonObject = new JSONObject(String.valueOf(o));
            ChoiceContestProblem problem = new ChoiceContestProblem();
            problem.setAlias(jsonObject.getString("alias"))
                    .setNum(jsonObject.getString("num"))
                    .setOj(jsonObject.getString("oj"))
                    .setProblemNum(jsonObject.getString("problemNum"));
            list.add(problem);
        }
        for (ChoiceContestProblem problem : list) {
            System.out.println(problem);
        }
        System.out.println(contest);

        return contestService.contestAdd(contest, list);
    }


    /**
     * 修改比赛
     *
     * @param contest
     * @param
     * @return
     * @throws Exception
     */
    @PutMapping("/{cid}")
    @ResponseBody
    @RequiresAuthentication
    public Info contestUpdate(@RequestBody Contest contest) throws Exception {
        System.out.println(contest);
        List<ChoiceContestProblem> list = new ArrayList<>();
        String problemList = contest.getProblemList();
        JSONArray objects = new JSONArray(problemList);
        System.out.println(objects.length());
        for (int i = 0; i < objects.length(); i++) {
            Object o = objects.get(i);
            JSONObject jsonObject = new JSONObject(String.valueOf(o));
            ChoiceContestProblem problem = new ChoiceContestProblem();
            problem.setAlias(jsonObject.getString("alias"))
                    .setNum(jsonObject.getString("num"))
                    .setOj(jsonObject.getString("oj"))
                    .setProblemNum(jsonObject.getString("problemNum"));
            list.add(problem);
        }
        for (ChoiceContestProblem problem : list) {
            System.out.println(problem);
        }
        return contestService.contestUpdate(contest, list);
    }


    @DeleteMapping("/{cid}")
    @ResponseBody
    @RequiresAuthentication
    public Info contestDelete(@PathVariable("cid") Integer cid) {

        Info info = new Info();
        try {
            info = contestService.contestDelete(cid);
        } catch (Exception e) {
            e.printStackTrace();
            info.error(e.getMessage());
        }
        return info;
    }



    @PostMapping("/{cid}/login")
    @ResponseBody
    @RequiresAuthentication
    public Info contestLogin(@PathVariable("cid") Integer cid,@RequestParam("password") String password,HttpSession session){
        Info info = new Info();
        User user = (User) session.getAttribute("user");

        try {
            info=contestService.contestLogin(cid,password,user);
        }catch (Exception e){
            info.error(e.getMessage());
        }
        return info;
    }


    /**
     * 还未加权限控制，
     * public Contest，比赛开始后，用户进入，自动参加比赛(包括比赛所有者)，添加到 表participate中。
     * Protect Contest和Private Contest比赛，用户输入密码后，自动参加比赛，添加到 表participate中。
     * TODO: 添加比赛查看权限
     * 暂时添加权限判断 待测试
     * @param model
     * @param cid
     * @return
     */
    @GetMapping("/{cid}")
    public String contest(Model model, @PathVariable(value = "cid") Integer cid,HttpSession session,HttpServletResponse response) {
        User user = (User) session.getAttribute("user");
        System.out.println("判断一下");
        Contest contest = contestService.getById(cid);
        if(ObjectUtils.isEmpty(contest)){
            response.setStatus(404);
            return "error/404";
        }

        long beginTime = contest.getBeginTime().getTime();
        long endTime = contest.getEndTime().getTime();
        long currentTime = System.currentTimeMillis();
        long percent = (long) 100.0 * (currentTime - beginTime) / (endTime - beginTime);

        if (percent <= 0)
            percent = 0;
        if (percent >= 100)
            percent = 100;

        System.out.println(percent);
        model.addAttribute("contest", contest);
        model.addAttribute("percent", percent);

        Info info = new Info();
        try {
           info = contestService.checkAuthority(user, cid);
        }catch (Exception e){
            info.error("false");
        }

        if(((Boolean) info.get("flag"))==false){

            return "contest/contestLogin";
        }

        QueryWrapper<ContestProblem> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", cid);
        List<ContestProblem> list = contestProblemService.list(wrapper);
        contest.setAnnouncement(MarkdownUtils.markdownToHtml(contest.getAnnouncement()));
        contest.setDescription(MarkdownUtils.markdownToHtml(contest.getDescription()));
        model.addAttribute("problemList", list);
        System.out.println(list);


        // 比赛中每到题目的提交统计

        Map<String, Pair<Integer, Integer>> stringPairMap1 = contestService.contestSolvedProblemStatistic(cid);

        model.addAttribute("contestSubmitStatistic",stringPairMap1);

        //如果处于登录状态，统计用户是否过题。

        if(!ObjectUtils.isEmpty(user)){
            Map<String, Pair<Boolean, Boolean>> stringPairMap = contestService.contestUserSolvedProblemStatistic(cid, user.getUid());

            System.out.println(stringPairMap);
            model.addAttribute("userSubmitStatistic",stringPairMap);


        }


        return "contest/contestOverview";
    }



    /**
     * TODO:还未加权限判断 添加比赛查看权限
     * 非比赛主持人，当比赛还未开始，跳转到比赛首页，比赛开始则进入正常题目界面。
     * 2020-06-27 18:28:00 添加权限判读，尚未验证
     * @param cid
     * @param num
     * @param model
     * @return
     */
    @GetMapping("/{cid}/problem/{num}")
    public String contestProblem(@PathVariable("cid") Integer cid, @PathVariable("num") String num, Model model,
                                 HttpSession session, HttpServletResponse response) {
        User user = (User) session.getAttribute("user");
        Contest contest = contestService.getById(cid);
        // 如果比赛不存在，跳转到404
        if(ObjectUtils.isEmpty(contest)){
            response.setStatus(404);
            return "error/404";
        }

        // 如果比赛还没开始，并且不是比赛的创建这，则跳转到比赛首页，
        // 不过除非恶意搞怪，不然正常访问，非登录状态或者比赛为开始，是不会访问到这里的
        if (contest.getBeginTime().getTime() > System.currentTimeMillis()) {
            if (ObjectUtils.isEmpty(user) || !contest.getUid().equals(user.getUid())) {
                return "redirect:/contest/" + cid;
            }
        }


        QueryWrapper<ContestProblem> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", cid).eq("num", num);
        ContestProblem cp = contestProblemService.getOne(wrapper);

        contest.setAnnouncement(MarkdownUtils.markdownToHtml(contest.getAnnouncement()));
        contest.setDescription(MarkdownUtils.markdownToHtml(contest.getDescription()));
        model.addAttribute("contest", contest);
        /*
        比赛信息头
         */
        long beginTime = contest.getBeginTime().getTime();
        long endTime = contest.getEndTime().getTime();
        long currentTime = System.currentTimeMillis();
        long percent = (long) 100.0 * (currentTime - beginTime) / (endTime - beginTime);

        if (percent <= 0)
            percent = 0;
        if (percent >= 100)
            percent = 100;

        System.out.println(percent);
        model.addAttribute("percent", percent);


        Info info = new Info();
        try {
            info=contestService.checkAuthority(user,cid);
        }catch (Exception e){
            info.error("false");
        }

        if(((Boolean)info.get("flag"))==false){
            return "contest/contestLogin";
        }

        /*
            查询题目列表
         */
        wrapper.clear();
        wrapper.eq("cid", cid);
        List<ContestProblem> list = contestProblemService.list(wrapper);

        model.addAttribute("cpList", list);



        /*
            若用户登录，获取改用户，获取改题目最近几次的提交记录，默认5个
         */

        if (user != null) {
            MyPage<Submission> submissionMyPage = contestService.contestProblemLatestSubmitPage(cid, num,user.getUid());
            model.addAttribute("latestSubmit", submissionMyPage.getRecords());

        }



        /*
            获取题目显示页面的必要信息
         */
        Problem problem = cp.getProblem();
        problem.setIOFormat(Constants.lf.get(problem.getOriginOJ()));
        model.addAttribute("cp", cp);
        model.addAttribute("problem", problem);
        model.addAttribute("description", problem.getDescription());
        model.addAttribute("tags", problem.getTags());
        model.addAttribute("aceLanguage", Constants.aceOjLanguageList.get(problem.getOriginOJ()));
        model.addAttribute("ojLanguage", Constants.ojLanguageList.get(problem.getOriginOJ()));

        return "contest/contestProblem";
    }




    /**
     * TODO:还未加权限判断 添加比赛查看权限
     * 非比赛主持人，当比赛还未开始，跳转到比赛首页，比赛开始则进入正常题目界面。
     * 2020-06-27 18:28:00 添加权限判读，尚未验证
     * @param cid
     * @param model
     * @return
     */
    @GetMapping("/{cid}/rank")
    public String contestRank(@PathVariable("cid") Integer cid, Model model,
                                 HttpSession session, HttpServletResponse response) {
        User user = (User) session.getAttribute("user");
        Contest contest = contestService.getById(cid);
        // 如果比赛不存在，跳转到404
        if(ObjectUtils.isEmpty(contest)){
            response.setStatus(404);
            return "error/404";
        }

        // 如果比赛还没开始，并且不是比赛的创建这，则跳转到比赛首页，
        // 不过除非恶意搞怪，不然正常访问，非登录状态或者比赛为开始，是不会访问到这里的
        if (contest.getBeginTime().getTime() > System.currentTimeMillis()) {
            if (ObjectUtils.isEmpty(user) || !contest.getUid().equals(user.getUid())) {
                return "redirect:/contest/" + cid;
            }
        }


        contest.setAnnouncement(MarkdownUtils.markdownToHtml(contest.getAnnouncement()));
        contest.setDescription(MarkdownUtils.markdownToHtml(contest.getDescription()));
        model.addAttribute("contest", contest);
        /*
        比赛信息头
         */
        long beginTime = contest.getBeginTime().getTime();
        long endTime = contest.getEndTime().getTime();
        long currentTime = System.currentTimeMillis();
        long percent = (long) 100.0 * (currentTime - beginTime) / (endTime - beginTime);

        if (percent <= 0)
            percent = 0;
        if (percent >= 100)
            percent = 100;

        System.out.println(percent);
        model.addAttribute("percent", percent);


        Info info = new Info();
        try {
            info=contestService.checkAuthority(user,cid);
        }catch (Exception e){
            info.error("false");
        }

        if(((Boolean)info.get("flag"))==false){
            return "contest/contestLogin";
        }



        /*
          获取榜单信息

         */

        List<Participant> ranks = contestService.contestRank(cid);
        model.addAttribute("ranks",ranks);

        //获取题目列表
        QueryWrapper<ContestProblem> cpWrapper = new QueryWrapper<>();
        cpWrapper.eq("cid", cid);
        List<ContestProblem> problemList = contestProblemService.list(cpWrapper);
        model.addAttribute("problemList",problemList    );

        return "contest/contestRank";
    }



    /**
     *
     * TODO:还未加权限判断 添加比赛查看权限
     * 提交状态 页面
     *
     * @param contestStatusVo
     * @param model
     * @return
     */
    @GetMapping({"/{cid}/status", "/{cid}/status/list/{page}"})
    public String status(ContestStatusVo contestStatusVo, Model model,HttpServletResponse response,HttpSession session) {
        Contest contest = contestService.getById(contestStatusVo.getCid());
        if(ObjectUtils.isEmpty(contest)){
            response.setStatus(404);
            return "error/404";
        }
        Info info = statusPage(contestStatusVo, model, session);
        if(((Boolean)info.get("flag"))==false){
            return "contest/contestLogin";
        }
        return "contest/contestStatus";
    }



    /**
     *
     * TODO:还未加权限判断 添加比赛查看权限
     *
     * 暂时添加好了，待测试
     *
     * 自己的提交状态 页面
     *
     * @param contestStatusVo
     * @param model
     * @return
     */
    @GetMapping({"/{cid}/my", "/{cid}/my/list/{page}"})
    @RequiresAuthentication
    public String myStatus(ContestStatusVo contestStatusVo, Model model, HttpSession session,HttpServletResponse response) {
        Contest contest = contestService.getById(contestStatusVo.getCid());
        if(ObjectUtils.isEmpty(contest)){
            response.setStatus(404);
            return "error/404";
        }
        contestStatusVo.setIsSelf(true);
        User user = (User) session.getAttribute("user");
        if (!ObjectUtils.isEmpty(user)) {
            contestStatusVo.setUid(user.getUid());
        } else {
            contestStatusVo.setUid(null);
        }
        Info info = statusPage(contestStatusVo, model, session);
        if(((Boolean)info.get("flag"))==false){
            return "contest/contestLogin";
        }
        return "contest/contestMyStatus";
    }



    @GetMapping("/{cid}/statistic/{num}")
    @ResponseBody
    public Info contestSubmitStatistic(@PathVariable("cid") Integer cid,@PathVariable("num") String num){
        Info info = new Info();
        try {
            info.put("data",contestService.contestSubmissionStatistic(cid,num));
            info.ok("success");
        }catch (Exception e){
            info.error(e.getMessage());
        }
        return info;
    }


    /**
     * 比赛 代码提交
     *
     * @param cid
     * @param num
     * @param model
     * @param submission
     * @param session
     * @return
     */
    @PostMapping("/{cid}/submit/{num}")
    @ResponseBody
    @RequiresAuthentication
    public Info contestSubmit(@PathVariable("cid") Integer cid, @PathVariable("num") String num, Model model,
                              Submission submission, HttpSession session) {


        Info info = new Info();

        User user = (User) session.getAttribute("user");
        if (ObjectUtils.isEmpty(submission)) {
            return new Info().error("The submit is empty");
        }

        try {
            info=contestService.checkAuthority(user,cid);
        }catch (Exception e){
            info.error("Permission denied");
        }

        if(((Boolean)info.get("flag"))==false){
            return info;
        }


        /*
        TODO: originOJ 和 pid要根据cid和num去查找得到。
          不能直接从前端传过来，否则就就没意义了。

          已经完成
         */
        QueryWrapper<ContestProblem> wrapper = new QueryWrapper<>();
        wrapper.eq("num", num).eq("cid", cid);
        ContestProblem contestProblem = contestProblemService.getOne(wrapper);

        // 设置pid。
        submission.setPid(contestProblem.getProblem().getPid());
        submission.setContestId(cid);
        /*
           通过OJ名获取对应OJ的languageList，然后映射到对应显示的名称。
                1. C++
                2. C
                3. Java
                4. Python
                5. other
         */
        submission.setDisplayLanguage(Tools.findShowLanguage(Constants.ojLanguageList.get(contestProblem.getProblem().getOriginOJ()).get(submission.getLanguage())));
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


        submission.setUid(user.getUid());
        submission.setSubTime(new Date());
        System.out.println(submission);
        submission.setLength(submission.getSourceCode().length());

        try {
            info = submissionService.submissionSave(submission, contestProblem.getProblem().getOriginOJ());

        } catch (Exception e) {
            info.error("submit fail,try again!");
        }
        return info;

    }






    public Info statusPage(ContestStatusVo contestStatusVo, Model model,HttpSession session) {

        Info info = new Info();
        User user = (User) session.getAttribute("user");
        Contest contest = contestService.getById(contestStatusVo.getCid());
        contest.setAnnouncement(MarkdownUtils.markdownToHtml(contest.getAnnouncement()));
        contest.setDescription(MarkdownUtils.markdownToHtml(contest.getDescription()));
        model.addAttribute("contest", contest);
        long beginTime = contest.getBeginTime().getTime();
        long endTime = contest.getEndTime().getTime();
        long currentTime = System.currentTimeMillis();
        long percent = (long) 100.0 * (currentTime - beginTime) / (endTime - beginTime);

        if (percent <= 0)
            percent = 0;
        if (percent >= 100)
            percent = 100;

        System.out.println(percent);
        model.addAttribute("percent", percent);

        try {
            info=contestService.checkAuthority(user,contestStatusVo.getCid() );
        }catch (Exception e){
            info.error("false");
        }

        if(((Boolean)info.get("flag"))==false){
            return info;
        }

        MyPage<Submission> submissionMyPage = contestService.contestStatusListPage(contestStatusVo);
        List<Submission> submissions = submissionMyPage.getRecords();
        System.out.println("Status hello world ==============");
        model.addAttribute("submissionsList", submissions);
        model.addAttribute("page", submissionMyPage);

        return info.ok("ok");
    }


    /**
     * TODO:还未加权限判断 添加比赛查看权限
     * 比赛评论
     *
     * @param cid
     * @param model
     * @return
     */
    @GetMapping("/{cid}/comment")
    public String comment(@PathVariable("cid") Integer cid, Model model,HttpServletResponse response,HttpSession session) {
        User user = (User) session.getAttribute("user");
        Contest contest = contestService.getById(cid);
        if(ObjectUtils.isEmpty(contest)){
            response.setStatus(404);
            return "error/404";
        }
        model.addAttribute("contest", contest);

        long beginTime = contest.getBeginTime().getTime();
        long endTime = contest.getEndTime().getTime();
        long currentTime = System.currentTimeMillis();
        long percent = (long) 100.0 * (currentTime - beginTime) / (endTime - beginTime);

        if (percent <= 0)
            percent = 0;
        if (percent >= 100)
            percent = 100;


        System.out.println(percent);
        model.addAttribute("percent", percent);
        Info info = new Info();
        try {
            info=contestService.checkAuthority(user,cid);
        }catch (Exception e){
            info.error("false");
        }
        if(((Boolean)info.get("flag"))==false){
            return "contest/contestLogin";
        }
        List<ContestComment> contestComments = contestCommentService.contestCommentList(cid);
        model.addAttribute("comments", contestComments);

        return "contest/contestComments";
    }


    /**
     * 提交评论
     *
     * @param contestComment
     * @param session
     * @return
     */
    @PostMapping("/{cid}/comment")
    @ResponseBody
    @RequiresAuthentication
    public Info postComment(ContestComment contestComment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        contestComment.setUid(user.getUid());

        Info info = new Info();
        try {
            info = contestCommentService.contestCommentSave(contestComment);
        } catch (Exception e) {
            info.error(e.getMessage());
        }
        return info;
    }
}
