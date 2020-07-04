package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.ContestComment;
import cn.jxj4869.joj.mapper.ContestCommentMapper;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.service.IContestCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
public class ContestCommentServiceImpl extends ServiceImpl<ContestCommentMapper, ContestComment> implements IContestCommentService {

    @Autowired
    private ContestCommentMapper commentMapper;

    @Override
    @Transactional
    public Info contestCommentSave(ContestComment contestComment) throws Exception {
        contestComment.setCreateTime(new Date());

        if (contestComment.getParentCommentId().equals(-1)) {
            contestComment.setParentCommentId(null);
        }
        if (contestComment.getReplyCommentId().equals(-1)) {
            contestComment.setReplyCommentId(null);
        }
        int insert = commentMapper.insert(contestComment);

        if (insert != 1) {
            throw new Exception("comment fail");
        }

        Info info = new Info();
        info.ok("success");

        return info;
    }

    @Override
    public List<ContestComment> contestCommentList(Integer cid) {
        List<ContestComment> contestComments = commentMapper.selectParentCommentNull(cid);

        return contestComments;
    }
}
