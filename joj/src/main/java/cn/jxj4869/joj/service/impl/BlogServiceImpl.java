package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.Blog;
import cn.jxj4869.joj.mapper.BlogMapper;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.pojo.vo.BlogVo;
import cn.jxj4869.joj.service.IBlogService;
import cn.jxj4869.joj.utils.Constants;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    @Autowired
    BlogMapper blogMapper;




    @Override
    public MyPage<Blog> blogListPage(BlogVo blogVo) {
        MyPage<Blog> page=new MyPage<>(blogVo.getPage(), Constants.DEFAULT_PAGE_SIZE);
        QueryWrapper<Blog> wrapper=new QueryWrapper<>();
        wrapper.like(blogVo.getWord()!=null,"title",blogVo.getWord()).eq(blogVo.getType().equals("self"),"uid",blogVo.getUid()).eq("published",true)
                .orderByDesc(blogVo.getOrderBy().equals("updateTime"),"updateTime").orderByDesc(blogVo.getOrderBy().equals("view"),"views")
                .orderByDesc(blogVo.getOrderBy().equals("default"),"updateTime");
        MyPage<Blog> blogMyPage = blogMapper.selectPage(page, wrapper);
        blogMyPage.setShowBtnNum();
        return blogMyPage;
    }

    @Override
    public MyPage<Blog> blogListPage(Integer uid, Integer pageSize, Integer currentPage) {
        MyPage<Blog> page=new MyPage<>(currentPage,pageSize);
        QueryWrapper<Blog> wrapper=new QueryWrapper<>();
        wrapper.eq("uid",uid).eq("published",true).orderByDesc("createTime");
        MyPage<Blog> blogMyPage = blogMapper.selectPage(page, wrapper);
        blogMyPage.setShowBtnNum();
        return blogMyPage;
    }

    @Override
    public MyPage<Blog> blogAllPage(Integer uid, Integer pageSize, Integer currentPage) {
        MyPage<Blog> page=new MyPage<>(currentPage,pageSize);
        QueryWrapper<Blog> wrapper=new QueryWrapper<>();
        wrapper.eq("uid",uid).orderByDesc("createTime");
        MyPage<Blog> blogMyPage = blogMapper.selectPage(page, wrapper);
        blogMyPage.setShowBtnNum();
        return blogMyPage;
    }

    @Override
    public MyPage<Blog> blogAllPage(Integer currentPage) {
        MyPage<Blog> page=new MyPage<>(currentPage,Constants.DEFAULT_PAGE_SIZE);
        QueryWrapper<Blog> wrapper=new QueryWrapper<>();
        wrapper.orderByDesc("createTime");
        MyPage<Blog> blogMyPage = blogMapper.selectPage(page, wrapper);
        blogMyPage.setShowBtnNum();
        return blogMyPage;
    }

    @Override
    @Transactional
    public Info blogDelete(Integer blogId) {
        Info info = new Info();
        int cnt = blogMapper.deleteById(blogId);
        if (cnt == 1) {
          info.ok("successfully deleted");
        } else {
            info.error("failed to delete");
        }
        return info;
    }

    /**
     * 新增博客的保存
     *
     * @param blog
     * @return
     */
    @Override
    @Transactional
    public Info blogSave(Blog blog) {
        Info info = new Info();
        blog.setViews(0);
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        int cnt = blogMapper.insert(blog);
        if (cnt == 1) {
            info.ok("Saved successfully");
        } else {
            info.error("Save failed");
        }
        return info;
    }


    @Override
    @Transactional
    public Info blogUpdate(Blog blog) {
        Info info = new Info();
        blog.setUpdateTime(new Date());
        int cnt = blogMapper.updateById(blog);
        if (cnt == 1) {
           info.ok("update completed");
        } else {
            info.error("Update failed");
        }

        return info;
    }

    @Override
    @Transactional
    public Blog getToViewById(Integer id) {
        Blog blog = blogMapper.getToViewById(id);
        if(ObjectUtils.isEmpty(blog)){
            return null;
        }
        blog.setViews(blog.getViews()+1);
        blogMapper.updateById(blog);

        return blog;
    }
}
