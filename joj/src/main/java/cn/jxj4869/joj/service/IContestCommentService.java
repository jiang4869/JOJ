package cn.jxj4869.joj.service;

import cn.jxj4869.joj.entity.ContestComment;
import cn.jxj4869.joj.entity.Message;
import cn.jxj4869.joj.pojo.Info;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface IContestCommentService extends IService<ContestComment> {

    public Info contestCommentSave(ContestComment contestComment) throws Exception;

    public List<ContestComment> contestCommentList(Integer cid);

}
