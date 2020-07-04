package cn.jxj4869.joj.service;

import cn.jxj4869.joj.entity.BlogComment;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface IBlogCommentService extends IService<BlogComment> {
    public Info commentSave(BlogComment comment);

    public MyPage<BlogComment> commentPage(Integer currentPage, Integer blogId);

    public Info commentDelete(Integer cid);

    public MyPage<BlogComment> commentAllPage(Integer currentPage);

}
