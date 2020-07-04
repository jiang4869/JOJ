package cn.jxj4869.joj.service;

import cn.jxj4869.joj.entity.User;
import cn.jxj4869.joj.pojo.Info;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface IUserService extends IService<User> {
    public Info register(User user);

    public Info findPassword(String userName, ServletContext servletContext);


    List<User> selectParticipateContest(@Param(Constants.WRAPPER) Wrapper<User> queryWrapper);
}
