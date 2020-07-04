package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.ContestProblem;
import cn.jxj4869.joj.entity.Description;
import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.mapper.ContestProblemMapper;
import cn.jxj4869.joj.pojo.Sample;
import cn.jxj4869.joj.service.IContestProblemService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class ContestProblemServiceImpl extends ServiceImpl<ContestProblemMapper, ContestProblem> implements IContestProblemService {

    @Autowired
    private ContestProblemMapper mapper;

    @Override
    public ContestProblem getOne(Wrapper<ContestProblem> queryWrapper) {

        ContestProblem cp = mapper.selectOne(queryWrapper);
        Problem problem = cp.getProblem();
        Description description = problem.getDescription();
        String samples = description.getSamples();
        List<Sample> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            list = mapper.readValue(samples, list.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        description.setSampleList(list);
        problem.setDescription(description);
        cp.setProblem(problem);
        return cp;
    }
}
