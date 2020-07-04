package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.Blog;
import cn.jxj4869.joj.entity.Description;
import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.mapper.ProblemMapper;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.pojo.Sample;
import cn.jxj4869.joj.pojo.vo.ProblemVo;
import cn.jxj4869.joj.service.IProblemService;
import cn.jxj4869.joj.utils.Constants;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IProblemService {

    @Autowired
    private ProblemMapper problemMapper;


    @Value("${joj.spider.uid}")
    private Integer spiderUid;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public MyPage<Problem> problemListPage(ProblemVo vo) {
        /*
            private String type="all"; // all, solved, attempted
            private String title; // 题目名
            private String OJ; //OJ名
            private String ProbId; // 题目在原OJ的id
            private Integer page=1; //当前页面
            private Integer uid;  //用户id
            private List<ProblemTag> tags; // 标签
         */

        List<Integer> list = new ArrayList<>();
        if (!StringUtils.isEmpty(vo.getTags())) {
            String[] s = vo.getTags().split(" ");
            for (String s1 : s) {
                list.add(Integer.parseInt(s1));
            }
        }
        if (!StringUtils.isEmpty(vo.getTitle())) {
            vo.setTitle(vo.getTitle().trim());
        }
        if (!StringUtils.isEmpty(vo.getOj())) {
            vo.setOj(vo.getOj().trim());
        }
        if (!StringUtils.isEmpty(vo.getProbId())) {
            vo.setProbId(vo.getProbId().trim());
        }
        MyPage<Problem> page = new MyPage<>(vo.getPage(), Constants.DEFAULT_PROBLEM_PAGE_SIZE);
        QueryWrapper<Problem> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(vo.getTitle()), "title", vo.getTitle())
                .like(!StringUtils.isEmpty(vo.getOj()) && !vo.getOj().equalsIgnoreCase("all"), "originOJ", vo.getOj())
                .like(!StringUtils.isEmpty(vo.getProbId()), "originProb", vo.getProbId())
                .in(!StringUtils.isEmpty(vo.getTags()), "ptags.id", list);

        MyPage<Problem> problemMyPage = problemMapper.selectPage(page, wrapper);
        problemMyPage.setShowBtnNum();
        return problemMyPage;
    }

    @Override
    public Problem selectById(Serializable id) throws JsonProcessingException {
        Problem problem = problemMapper.selectById(id);
        Description description = problem.getDescription();
        String samples = description.getSamples();
        List<Sample> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        list = mapper.readValue(samples, list.getClass());
        description.setSampleList(list);
        problem.setDescription(description);
        return problem;
    }


    @Override
    public List<Problem> selectSolvedProblemList(Serializable uid) {

        return problemMapper.selectSolvedProblemList(uid);
    }


    @Override
    public void spiderProblem(Problem problem) {

        problem.setUid(spiderUid);

        rabbitTemplate.convertAndSend("joj.spider","spider."+problem.getOriginOJ().toLowerCase(),problem);

    }
}
