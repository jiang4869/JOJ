package cn.jxj4869.joj.controller;


import cn.jxj4869.joj.entity.User;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.service.IUserService;
import cn.jxj4869.joj.shiro.ShiroMD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    IUserService userService;

    @PostMapping("/checkCode")
    public Map<String, Object> getCheckCode(String checkCode, HttpSession session) {
        Info info = new Info();
        String code = (String) session.getAttribute("checkCode");
        System.out.println(code);
        if (code != null && code.equalsIgnoreCase(checkCode)) {
            info.ok("success");
        } else {
            info.error("error");
        }
        return info;
    }


    @RequestMapping("/checkUserName")
    public Map<String, Object> checkUserName(String userName) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userName", userName);
        User user = userService.getOne(wrapper);
        Info info = new Info();
        if (user != null) {
            info.error("User already exists");
        } else {
            info.ok("success");
        }
        return info;
    }

    @RequestMapping("/checkEmail")
    public Info checkEmail(String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        User user = userService.getOne(wrapper);
        System.out.println("email " + user);
        Info info = new Info();
        if (user != null) {
            info.error("The email has been registered");
        } else {
            info.ok("success");
        }
        return info;
    }


    @RequestMapping("/checkOriginPassword")
    @ResponseBody
    @RequiresAuthentication
    public Info checkOriginPassword(String userName, String originPassword) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userName", userName);
        User user = userService.getOne(wrapper);
        System.out.println("userName " + user);
        Info info = new Info();
        Object o = ShiroMD5.MD5(userName, originPassword);
        if (user != null && user.getPassword().equals(String.valueOf(o))) {
            info.ok("success");
        } else {
            info.error("wrong password");
        }
        return info;
    }


    @PostMapping("/register")
    public Info register(User user) {
        System.out.println(user);

        return userService.register(user);
    }

    @PostMapping("/login")
    public Info login(@RequestParam("userName") String userName, @RequestParam("password") String password,
                       HttpSession session) {

        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);

        Subject subject = SecurityUtils.getSubject();
        Info info = new Info();
        try {
            subject.login(token);
            User user = (User) subject.getPrincipal();
            session.setAttribute("user", user);

            info.ok("login success");
        } catch (AuthenticationException e) {
            info.error("Username or password is incorrect");
        }

        return info;
    }


    @PutMapping("/{uid}")
    @ResponseBody
    @Transactional
    @RequiresAuthentication
    public Info updateUser(@RequestBody User user, HttpSession session) throws Exception {
        Info info = new Info();

        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(null);
        } else {
            Object o = ShiroMD5.MD5(user.getUserName(), user.getPassword());
            user.setPassword(String.valueOf(o));
        }

        boolean flag = userService.updateById(user);
        if (flag) {
            info.ok("update successfully");
            user = userService.getById(user.getUid());
            session.setAttribute("user", user);
        } else {
            throw new Exception("update fail");

        }
        return info;
    }


    @PostMapping("/findPassword")
    @ResponseBody
    public Info findPassword(@RequestParam("userName") String userName, HttpSession session) {
        Info info = new Info();
        info = userService.findPassword(userName, session.getServletContext());

        return info;
    }


    @PostMapping("/resetPassword")
    @Transactional
    public Info resetPassword(String userName, String password, String confirmCode, HttpSession session) throws Exception {
        ServletContext servletContext = session.getServletContext();
        String confirm = (String) servletContext.getAttribute(userName);
        Info info = new Info();
        if (StringUtils.isEmpty(confirmCode)||StringUtils.isEmpty(confirm) || !confirm.equals(confirmCode)) {
            info.error("confirmCode  error");
        } else {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("userName", userName);
            User user = userService.getOne(wrapper);
            Object o = ShiroMD5.MD5(user.getUserName(), password);
            user.setPassword(String.valueOf(o));
            boolean flag = userService.updateById(user);
            if (flag) {
                info.ok("update successfully");
                user = userService.getById(user.getUid());
                session.setAttribute("user", user);
                servletContext.removeAttribute(userName);
            } else {
                throw new Exception("update fail");

            }
        }
        return info;
    }


}
