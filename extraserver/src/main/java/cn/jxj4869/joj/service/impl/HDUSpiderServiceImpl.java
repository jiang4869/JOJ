package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.Description;
import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.entity.ProblemTags;
import cn.jxj4869.joj.mapper.DescriptionMapper;
import cn.jxj4869.joj.mapper.ProblemMapper;
import cn.jxj4869.joj.mapper.ProblemTagsMapper;
import cn.jxj4869.joj.service.ISpiderService;
import cn.jxj4869.joj.spider.HDUSpider;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.experimental.Accessors;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-10 22:07
 */

@Service
public class HDUSpiderServiceImpl implements ISpiderService {

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private DescriptionMapper descriptionMapper;

    @Autowired
    private ProblemTagsMapper problemTagsMapper;


    @Autowired
    private HDUSpider hduSpider;

    @Value("${joj.spider.uid}")
    private Integer spiderUid;

    @Value("${joj.spider.userName}")
    private String userName;

    @Value("${joj.default.tag.hdu}")
    private Integer tag;

    // 只有先查找数据库，没有找到对应的题目，才会发送到消息队列
    @Override
    @RabbitListener(queues = {"spider.hdu"})
    public void receive1(Problem problem) {
        try {
            crawl(problem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @RabbitListener(queues = {"spider.hdu"})
    public void receive2(Problem problem) {
        try {
            crawl(problem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    @RabbitListener(queues = {"spider.hdu"})
    public void receive3(Problem problem) {
        try {
            crawl(problem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 若为recrawl，则problem需要传递pid。
     * 约定 由爬虫账号抓取的题目 只有一个对应的描述信息  1对1
     *
     * @param problem
     * @throws Exception
     */
    @Override
    @Transactional
    public void crawl(Problem problem) throws Exception {


        QueryWrapper<Problem> problemWrapper = new QueryWrapper<>();
        problemWrapper.eq("originOJ", problem.getOriginOJ()).eq("originProb", problem.getOriginProb());
        Problem problem1 = problemMapper.selectOne(problemWrapper);


        Description description = hduSpider.crawl(problem);
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
