package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.Description;
import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.entity.ProblemTags;
import cn.jxj4869.joj.mapper.DescriptionMapper;
import cn.jxj4869.joj.mapper.ProblemMapper;
import cn.jxj4869.joj.mapper.ProblemTagsMapper;
import cn.jxj4869.joj.service.ISpiderService;
import cn.jxj4869.joj.spider.CodeForcesSpider;
import cn.jxj4869.joj.spider.HDUSpider;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiangXiaoju
 * @date 2020-06-11 17:43
 */

public class CodeForcesSpiderServiceImpl implements ISpiderService {
    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private DescriptionMapper descriptionMapper;

    @Autowired
    private ProblemTagsMapper problemTagsMapper;


    @Autowired
    private CodeForcesSpider codeForcesSpider;


    @Value("${joj.spider.uid}")
    private Integer spiderUid;

    @Value("${joj.spider.userName}")
    private String userName;


    @Value("${joj.default.tag.codeforces}")
    private Integer tag;

    @Override
    @RabbitListener(queues = {"spider.codeforces"})
    public void receive1(Problem problem) {
        try {
            crawl(problem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @RabbitListener(queues = {"spider.codeforces"})
    public void receive2(Problem problem) {
        try {
            crawl(problem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @RabbitListener(queues = {"spider.codeforces"})
    public void receive3(Problem problem) {
        try {
            crawl(problem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void crawl(Problem problem) throws Exception {


        QueryWrapper<Problem> problemWrapper = new QueryWrapper<>();
        problemWrapper.eq("originOJ", problem.getOriginOJ()).eq("originProb", problem.getOriginProb());
        Problem problem1 = problemMapper.selectOne(problemWrapper);


        Description description = codeForcesSpider.crawl(problem);
        if (description == null) {
            return;
        }
        int count;

        if (problem.getPid() == null && problem1 == null) {
            count = problemMapper.insert(problem);
            if (count != 1) {
                return;
            }
            description.setPid(problem.getPid());

            count = descriptionMapper.insert(description);
            //插入失败，发出异常，回滚事务
            if (count != 1) {
                throw new Exception();
            }
            // 插入默认标签
            ProblemTags problemTags = new ProblemTags();
            problemTags.setPid(problem.getPid());
            problemTags.setId(tag);

            int insert = problemTagsMapper.insert(problemTags);
            if(insert!=1){
                throw new Exception();
            }
        } else {
            // 为重新抓取题目，则更新题目和题目描述
            if (problem.getPid() == null) {
                problem.setPid(problem1.getPid());
            }
            problemMapper.updateById(problem);

            QueryWrapper<Description> wrapper = new QueryWrapper<>();
            wrapper.eq("pid", problem.getPid()).eq("uid", spiderUid);
            descriptionMapper.update(description, wrapper);
        }


    }
}
