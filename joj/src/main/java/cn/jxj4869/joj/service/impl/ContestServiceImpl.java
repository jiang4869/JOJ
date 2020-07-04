package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.*;
import cn.jxj4869.joj.mapper.ContestMapper;
import cn.jxj4869.joj.mapper.ContestProblemMapper;
import cn.jxj4869.joj.mapper.ProblemMapper;
import cn.jxj4869.joj.mapper.SubmissionMapper;
import cn.jxj4869.joj.pojo.*;
import cn.jxj4869.joj.pojo.vo.ContestStatusVo;
import cn.jxj4869.joj.pojo.vo.ContestVo;
import cn.jxj4869.joj.service.IContestProblemService;
import cn.jxj4869.joj.service.IContestService;
import cn.jxj4869.joj.service.IParticipateService;
import cn.jxj4869.joj.service.IUserService;
import cn.jxj4869.joj.utils.Constants;
import cn.jxj4869.joj.utils.Tools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Service
public class ContestServiceImpl extends ServiceImpl<ContestMapper, Contest> implements IContestService {

    @Autowired
    private ContestMapper contestMapper;


    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private SubmissionMapper submissionMapper;


    @Autowired
    private IContestProblemService contestProblemService;

    @Autowired
    private IParticipateService participateService;

    @Autowired
    private IUserService userService;

    @Override
    @Transactional
    public Info contestAdd(Contest contest, List<ChoiceContestProblem> list) throws Exception {
        int insert = contestMapper.insert(contest);
        if (insert != 1) {
            throw new Exception("Add contest fail!");
        }


        List<ContestProblem> problemList = new ArrayList<>();
        for (ChoiceContestProblem p : list) {
            ContestProblem problem = new ContestProblem();
            QueryWrapper<Problem> wrapper = new QueryWrapper<>();
            wrapper.eq("originOJ", p.getOj()).eq("originProb", p.getProblemNum());
            Problem one = problemMapper.selectOne(wrapper);
            problem.setAlias(p.getAlias())
                    .setCid(contest.getCid())
                    .setNum(p.getNum())
                    .setPid(one.getPid());
            problemList.add(problem);

        }
        boolean flag = contestProblemService.saveBatch(problemList);
        if (!flag) {
            throw new Exception("Add contest fail!");
        }

        Participate participate = new Participate();
        participate.setUid(contest.getUid()).setCid(contest.getCid());
        boolean save = participateService.save(participate);
        if (!save) {
            throw new Exception("Add contest fail!");
        }

        Info info = new Info();


        info.ok("success");
        info.put("data", contest);

        return info;
    }


    @Override
    @Transactional
    public Info contestUpdate(Contest contest, List<ChoiceContestProblem> list) throws Exception {

        int count = contestMapper.updateById(contest);

        if (count != 1) {
            throw new Exception("contest update fail");
        }

        QueryWrapper<ContestProblem> cpWrapper = new QueryWrapper<>();

        cpWrapper.eq("cid", contest.getCid());
        boolean remove = contestProblemService.remove(cpWrapper);
        if (!remove) {
            throw new Exception("contest update fail");
        }

        List<ContestProblem> problemList = new ArrayList<>();
        for (ChoiceContestProblem p : list) {
            ContestProblem problem = new ContestProblem();
            QueryWrapper<Problem> wrapper = new QueryWrapper<>();
            wrapper.eq("originOJ", p.getOj()).eq("originProb", p.getProblemNum());
            Problem one = problemMapper.selectOne(wrapper);
            problem.setAlias(p.getAlias())
                    .setCid(contest.getCid())
                    .setNum(p.getNum())
                    .setPid(one.getPid());
            problemList.add(problem);

        }
        boolean flag = contestProblemService.saveBatch(problemList);
        if (!flag) {
            throw new Exception("contest update fail");
        }
        Info info = new Info();
        info.ok("success");


        return info;
    }


    @Override
    @Transactional
    public Info contestDelete(Integer cid) throws Exception {
        int count = contestMapper.deleteById(cid);
        if (count != 1) {
            throw new Exception("delete fail");
        }

        Info info = new Info();
        info.ok("success");
        return info;
    }

    @Override
    public MyPage<Submission> contestStatusListPage(ContestStatusVo statusVo) {

        MyPage<Submission> page = new MyPage<>(statusVo.getPage(), Constants.DEFAULT_Submission_PAGE_SIZE);
        QueryWrapper<Submission> wrapper = new QueryWrapper<>();
        wrapper.eq("contestId", statusVo.getCid())
                .eq(statusVo.getIsSelf(), "joj_submission.uid", statusVo.getUid())
                .orderByDesc("subTime");
        MyPage<Submission> submissionMyPage = submissionMapper.selectContestStatusPage(page, wrapper);

        List<Submission> submissions = submissionMyPage.getRecords();
        Map<String, Map<String, String>> ojLanguageList = Constants.ojLanguageList;
        for (Submission submission : submissions) {
            Problem problem = submission.getProblem();
            submission.setTrueLanguage(ojLanguageList.get(problem.getOriginOJ()).get(submission.getLanguage()));
        }
        submissionMyPage.setRecords(submissions);
        submissionMyPage.setShowBtnNum();
        return submissionMyPage;
    }


    @Override
    public MyPage<Contest> contestListPage(ContestVo contestVo) {
        /*
            private String title;
            private Integer page=1;
            private String type; // all scheduled 0 running 1 end 2
            private Boolean isSelf=false; // false 查看全部，true 查看用户自己
            private Integer uid;
         */
        if (!StringUtils.isEmpty(contestVo.getTitle())) {
            contestVo.setTitle(contestVo.getTitle().trim());

        }

        MyPage<Contest> page = new MyPage<>(contestVo.getPage(), Constants.DEFAULT_CONTEST_PAGE_SIZE);
        QueryWrapper<Contest> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(contestVo.getTitle()), "title", contestVo.getTitle())
                .eq(contestVo.getIsSelf(), "uid", contestVo.getUid())
                .eq(!contestVo.getType().equals(-1), "status", contestVo.getType());
        MyPage<Contest> contestMyPage = contestMapper.selectPage(page, wrapper);
        contestMyPage.setShowBtnNum();
        return contestMyPage;
    }


    @Override
    public MyPage<Submission> contestProblemLatestSubmitPage(Integer cid, String num, Integer uid) {
        MyPage<Submission> page = new MyPage<>(1, Constants.DEFAULT_LATEST_SUBMIT_PAGE_SIZE);
        QueryWrapper<Submission> wrapper = new QueryWrapper<>();
        wrapper.eq("joj_contest.cid", cid).eq("num", num).orderByDesc("subTime").eq("joj_submission.uid", uid);
        MyPage<Submission> submissionMyPage = submissionMapper.selectContestStatusPage(page, wrapper);
        submissionMyPage.setShowBtnNum();

        return submissionMyPage;
    }

    /**
     * 进行比赛权限判断
     * <p>
     * 比赛的权限auth:
     * 1 public
     * 2 protect
     * 3 private
     *
     * @param user
     * @param cid
     * @return
     */
    @Override
    @Transactional
    public Info checkAuthority(User user, Serializable cid) throws Exception {
        Info info = new Info();

        Contest contest = contestMapper.selectById(cid);

        /*
         当比赛为public或者比赛为protect 但比赛已经结束。 则可以直接查看。
         */
        if (contest.getAuth() == 1 || contest.getAuth() == 2 && contest.getEndTime().getTime() <= System.currentTimeMillis()) {
            info.ok("ok");

            /*
              如果用户不为空，并且用户不在表joj_participate表中，则添加到joj_participate 中
             */
            if (!ObjectUtils.isEmpty(user)) {
                QueryWrapper<Participate> wrapper = new QueryWrapper<>();
                wrapper.eq("uid", user.getUid()).eq("cid", cid);
                Participate one = participateService.getOne(wrapper);
                if (ObjectUtils.isEmpty(one)) {
                    Participate participate = new Participate();
                    participate.setCid((Integer) cid).setUid(user.getUid());
                    boolean save = participateService.save(participate);
                    if (!save) {
                        throw new Exception("error");
                    }
                }
            }
            return info;
        }

        /*
            当比赛为 protect或者比赛为protect时
            如果用户未登录直接返回false

            如果用户登录，判断是否在表joj_participate中，若不存在 ，返回false。若存在返回true。
         */

        if (ObjectUtils.isEmpty(user)) {
            info.error("UnAuthority");
            return info;
        }


        QueryWrapper<Participate> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", user.getUid()).eq("cid", cid);
        Participate one = participateService.getOne(wrapper);
        /*
         不在表joj_participate中，返回false
         */
        if (ObjectUtils.isEmpty(one)) {
            info.error("UnAuthority");
            return info;
        }

        /*
            最后返回true了
         */

        info.ok("ok");

        return info;
    }


    @Override
    @Transactional
    public Info contestLogin(Serializable cid, String password, User user) throws Exception {
        Info info = new Info();

        Contest contest = contestMapper.selectById(cid);
        if (contest.getPassword().equals(password)) {
            Participate participate = new Participate();
            participate.setCid((Integer) cid).setUid(user.getUid());
            boolean save = participateService.save(participate);
            if (!save) {
                throw new Exception("An error occurred，try again !!!");
            }

            info.ok("success");
        } else {
            info.error("wrong password");
        }

        return info;
    }


    @Override
    public List<LinkedHashMap<Object, Object>> contestSubmissionStatistic(Serializable cid, String num) {
        QueryWrapper<ContestProblem> problemQueryWrapper = new QueryWrapper<>();
        problemQueryWrapper.eq("cid", cid).eq("num", num);
        ContestProblem one = contestProblemService.getOne(problemQueryWrapper);


        QueryWrapper<Submission> wrapper = new QueryWrapper<>();
        wrapper.eq("contestId", cid).eq("pid", one.getProblem().getPid());
        List<LinkedHashMap<Object, Object>> maps = submissionMapper.submissionStatistic(wrapper);
        for (Map<Object, Object> map : maps) {
            System.out.println(map);
        }
        return maps;
    }


    @Override
    public Map<String, Pair<Boolean, Boolean>> contestUserSolvedProblemStatistic(Serializable cid, Serializable uid) {

        /*

         这个功能就是一个简单的模拟题。

          1. 查询用户在该比赛中的所有提交记录
          2. 遍历每个提交 如果某道题目有提交记录，则 attempted  设置为true。  如果 通过，则accepted 键值为true。

         */

        Map<String, Pair<Boolean, Boolean>> map = new HashMap<>();

        QueryWrapper<Submission> wrapper = new QueryWrapper<>();
        wrapper.eq("contestId", cid).eq("joj_submission.uid", uid);
        List<Submission> submissions = submissionMapper.selectList(wrapper);
        // pair中，first表示 attempted  ，second 表示accepted
        for (Submission submission : submissions) {

            ContestProblem contestProblem = submission.getContestProblem();
            // 之前提交的题目现在已经被删除
            if (ObjectUtils.isEmpty(contestProblem)) {
                continue;
            }
            String num = contestProblem.getNum();
            if (ObjectUtils.isEmpty(map.get(num))) {
                map.put(num, new Pair<Boolean, Boolean>(true, false));
            }
            if (submission.getStatusType().equals(1)) {
                Pair<Boolean, Boolean> pair = map.get(num);
                pair.setSecond(true);
                map.put(num, pair);
            }


        }

        return map;
    }


    /**
     * 统计每道题提交过的人数和通过的人数
     *
     * @param cid
     * @return
     */
    @Override
    public Map<String, Pair<Integer, Integer>> contestSolvedProblemStatistic(Serializable cid) {

        /*
          1. 获取改比赛中的所有提交记录。
          2. 获取题目列表
          3. 遍历记录进行统计
         */


        // 记录每道题的提交人数和过题数 pair中，first表示 accepted  ，second 表示submitSum
        Map<String, Pair<Integer, Integer>> map = new HashMap<>();

        QueryWrapper<Submission> wrapper = new QueryWrapper<>();
        wrapper.eq("contestId", cid);
        // 获取提交记录
        List<Submission> submissions = submissionMapper.selectList(wrapper);

        // 获取题目列表

        QueryWrapper<ContestProblem> problemQueryWrapper = new QueryWrapper<>();
        problemQueryWrapper.eq("cid", cid);
        List<ContestProblem> problems = contestProblemService.list(problemQueryWrapper);

        for (ContestProblem problem : problems) {

            Set<Integer> userSubmitSet = new HashSet<>();
            Set<Integer> userAcceptedSet = new HashSet<>();
            for (Submission submission : submissions) {
                ContestProblem contestProblem = submission.getContestProblem();
                // 之前提交过的题目现在已经被删除了。
                if (ObjectUtils.isEmpty(contestProblem)) {
                    continue;
                }

                // 判断当前提交与题目是否是同一道题
                if (problem.getCpid().equals(contestProblem.getCpid())) {
                    userSubmitSet.add(submission.getUid());
                    if (submission.getStatusType().equals(1)) {
                        userAcceptedSet.add(submission.getUid());
                    }
                }
            }

            map.put(problem.getNum(),new Pair<>(userAcceptedSet.size(),userSubmitSet.size()));
        }




        return map;
    }


    /**
     * 获取排行榜
     *
     * @param cid
     * @return
     */
    @Override
    public List<Participant> contestRank(Serializable cid) {
        /*
            1. 获取题目题目列表
            2. 获取提交列表
            3. 获取多少人参加比赛（从joj_participate中获取）
            4. 遍历每一个人，计算每个人的信息。
            1. 单独计算每道题的提交信息。（遍历）
            2. 找出每道题第一次AC时间
            3. 遍历的所有提交，计算AC前提交次数，作为罚时。
            5. 找出First Blood

            // List<User> //每个在joj_participate表中的人
            class participant{
                private User user;
                private Integer score;
                private Integer time;
                private Integer sortTime;
                private List<ContestProblem> problemList;
            };

         */

        // 获取题目列表
        QueryWrapper<ContestProblem> cpWrapper = new QueryWrapper<>();
        cpWrapper.eq("cid", cid);
        List<ContestProblem> problemList = contestProblemService.list(cpWrapper);

        // 获取比赛信息
        Contest contest = contestMapper.selectById(cid);

        //获取提交列表
        QueryWrapper<Submission> sWrapper = new QueryWrapper<>();
        /*
            1. AC  Accepted | Accepted   #21ba45
            2. PE  Presentation Error | Presentation Error #e03997
            3. WA  Wrong Answer (on...) | Wrong Answer #db2828
            4. TLE Time Limit Exceeded | Time Limit Exceeded #f2711c
            5. MLE Memory Limit Exceeded | Memory Limit Exceeded #2185d0
            6. OLE Output Limit Exceeded | Output Limit Exceeded #00b5ad
            7. RE  Runtime Error | Runtime Error #a333c8
            8. CE  Compilation Error | Compilation Error #fbbd08
            9. UE  Unknown Error |   #1b1c1d
            10. Running & Judging   #767676
            11. Pending #e0e1e2 // 提交后，保存到数据库中的状态。
         */
        // 排除一些提交状态不参与排名计算 例如 编译错误 提交中等状态
        List<Integer> list=new ArrayList<>();
        list.add(8);
        list.add(9);
        list.add(10);
        list.add(11);
        sWrapper.eq("contestId", cid).notIn(!CollectionUtils.isEmpty(list),"statusType",list).apply("subTime>=beginTime and subTime <=endTime");

        List<Submission> submissionList = submissionMapper.selectList(sWrapper);


        // 获取参加用户列表

        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("cid", cid);

        List<User> users = userService.selectParticipateContest(userWrapper);

        List<Participant> data = new ArrayList<>();

        for (User user : users) {

            Integer acNum = 0;
            Long faShi = 0l;

            Participant participant = new Participant(user, 0, 0l, 0l, "", new LinkedHashMap<String, ProblemInfo>());


            //第一个代表AC时间，第二个代表罚时次数
            Map<String, Pair<Long, Integer>> problemDataList = new LinkedHashMap();

            // 计算每到题目的信息
            for (ContestProblem problem : problemList) {
                problemDataList.put(problem.getNum(), new Pair<>(5552304570991l, 0));
            }

            // 找出每道题目AC时间
            for (Submission submission : submissionList) {
                if (submission.getUid().equals(user.getUid())) {
                    // Accepted 状态码为 1     判断此次提交的题目是否还存在
                    if (submission.getStatusType().equals(1) && !ObjectUtils.isEmpty(submission.getContestProblem())) {

                        Pair<Long, Integer> pair = problemDataList.get(submission.getContestProblem().getNum());
                        Long time = pair.getFirst();
                        if (submission.getSubTime().getTime() < time) {
                            pair.setFirst(submission.getSubTime().getTime());
                            problemDataList.put(submission.getContestProblem().getNum(), pair);
                        }
                    }

                }
            }


            // 找出每道题AC前的提交次数，作为罚时
            for (Submission submission : submissionList) {
                if (submission.getUid().equals(user.getUid())) {
                    // Accepted 状态码为 1,不为1 则是提交失败 // 判断此次提交的题目是否还存在
                    if (!submission.getStatusType().equals(1) && !ObjectUtils.isEmpty(submission.getContestProblem())) {
                        Pair<Long, Integer> pair = problemDataList.get(submission.getContestProblem().getNum());
                        Long time = pair.getFirst();
                        if (submission.getSubTime().getTime() < time) {
                            pair.setSecond(pair.getSecond() + 1);
                            problemDataList.put(submission.getContestProblem().getNum(), pair);
                        }
                    }

                }
            }

            Map<String, ProblemInfo> problemInfoMap = new LinkedHashMap<>();

            //计算每一道题的信息
            for (ContestProblem problem : problemList) {
                ProblemInfo problemInfo = new ProblemInfo();

                /*
                    private Long faShi;
                    private Integer faShiNum;
                    private Boolean ac;
                 */
                // 如果AC了
                if (!problemDataList.get(problem.getNum()).getFirst().equals(5552304570991l)) {
                    acNum++;
                    // 计算罚时
                    Long acTime = problemDataList.get(problem.getNum()).getFirst();
                    Integer faShiNum = problemDataList.get(problem.getNum()).getSecond();
                    Long cha = (acTime - contest.getBeginTime().getTime()) / 1000;  //单位是秒
                    faShi += cha;
                    faShi += 20l * 60l * faShiNum; //单位是秒
                    problemInfo.setAc(true);
                    problemInfo.setFaShiNum(faShiNum);
//                    problemInfo.setFaShi(20l * faShiNum+cha);
                    problemInfo.setFaShi(Tools.transPeriod((cha + 20l * 60l * faShiNum) * 1000, false));
                } else {
                    problemInfo.setAc(false);
                    Integer faShiNum = problemDataList.get(problem.getNum()).getSecond();
                    problemInfo.setFaShiNum(faShiNum);
                }
                problemInfoMap.put(problem.getNum(), problemInfo);
            }

            participant.setSortTime(faShi);
            participant.setSolved(acNum);
            participant.setProblemDataList(problemInfoMap);
            participant.setPenalty(Tools.transPeriod(faShi * 1000, false));
            data.add(participant);
        }

        Collections.sort(data, (o1, o2) -> {
            if (!o1.getSolved().equals(o2.getSolved())) {
                return o2.getSolved().compareTo(o1.getSolved());
            } else {
                return o1.getSortTime().compareTo(o2.getSortTime());
            }
        });

        return data;
    }
}
