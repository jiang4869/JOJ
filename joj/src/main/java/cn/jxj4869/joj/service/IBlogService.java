package cn.jxj4869.joj.service;

import cn.jxj4869.joj.entity.Blog;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.pojo.vo.BlogVo;
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
public interface IBlogService extends IService<Blog> {

    public Info blogSave(Blog blog);

    public Info blogUpdate(Blog blog);

    public Info blogDelete(Integer blogId);


    public Blog getToViewById(Integer id);

    public MyPage<Blog> blogListPage(BlogVo blogVo);

    public MyPage<Blog> blogListPage(Integer uid,Integer pageSize,Integer currentPage);

    public MyPage<Blog> blogAllPage(Integer uid,Integer pageSize,Integer currentPage);

    public MyPage<Blog> blogAllPage(Integer currentPage);



}
