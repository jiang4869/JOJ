package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.BlogComment;
import cn.jxj4869.joj.entity.User;
import cn.jxj4869.joj.mapper.UserMapper;
import cn.jxj4869.joj.pojo.Email;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.service.IUserService;
import cn.jxj4869.joj.shiro.ShiroMD5;

import cn.jxj4869.joj.utils.Tools;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.experimental.Accessors;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletContext;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Value("${server.localhost}")
    private String localhost;

    @Override
    @Transactional
    public Info register(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userName", user.getUserName()).or().eq("email", user.getEmail());
        Info info = new Info();
        User checkUser = userMapper.selectOne(wrapper);
        // 再次验证邮箱用户名是否重复
        if (checkUser != null) {
            info.error("Username or email has been registered");
        } else {
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setAvatar(Tools.getAvatar());
            Object o = ShiroMD5.MD5(user.getUserName(), user.getPassword());
            user.setPassword(String.valueOf(o));
            int count = userMapper.insert(user);
            if (count == 1) {
                info.ok("success");
            } else {
                info.error("error,try again");
            }
        }
        return info;
    }


    @Async
    public void senderEmail(User user,String confirmCode,String contextPath) {
/*
Password reset link has been sent. Please check you email (1***0@qq.com), including your spam folder
 */

        String content = "Hi "+user.getNickName()+",<br>\n" +
                "Please click <a href=\""+localhost+contextPath+"/findPassword?userName="+user.getUserName()+"&confirmCode="+confirmCode+"\">[HERE]</a> to reset password for account <a href=\""+localhost+contextPath+"/console/"+user.getUserName()+"\">["+user.getUserName()+"]</a> in JOJ.\n" +
                "\n";
        Email email = new Email();
        email.setText(content);
        email.setSubject("Reset Password");
        email.setTo(user.getEmail());
        rabbitTemplate.convertAndSend("joj.email", "email", email);
    }


    @Override
    public Info findPassword(String userName, ServletContext servletContext) {
        Info info = new Info();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userName", userName);
        User user = userMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(user)) {
            info.error("User [" + userName + "] does not exist.");
        } else {
            String confirmCode = UUID.randomUUID().toString().replaceAll("-", "");
            servletContext.setAttribute(userName, confirmCode);
            String contextPath = servletContext.getContextPath();
            senderEmail(user,confirmCode,contextPath);
            String email = user.getEmail();
            email=email.replaceAll("(?<=.{2})[^@]+(?=.{2}@)","******");
            info.ok("Password reset link has been sent. Please check you email ("+email+"), including your spam folder");
        }
        return info;
    }

    @Override
    public List<User> selectParticipateContest(Wrapper<User> queryWrapper) {
        return userMapper.selectParticipateContest(queryWrapper);
    }
}
