package cn.jxj4869.joj.service;

import cn.jxj4869.joj.entity.Blog;
import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.pojo.vo.BlogVo;
import cn.jxj4869.joj.pojo.vo.ProblemVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface IProblemService extends IService<Problem> {
    public MyPage<Problem> problemListPage(ProblemVo problemVo);

    public Problem selectById(Serializable id) throws JsonProcessingException;

    public List<Problem> selectSolvedProblemList(Serializable uid);


    public void spiderProblem(Problem problem);



}
