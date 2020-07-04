package cn.jxj4869.joj.controller;

import cn.jxj4869.joj.pojo.Info;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author JiangXiaoju
 * @date 2020-06-12 17:22
 */
@ControllerAdvice
public class MyExceptionHandler {


    @ExceptionHandler({AuthorizationException.class})

    public String shiroException(HttpServletRequest request, HttpServletResponse response, Exception e){
        request.setAttribute("msg","Permission denied ，please login again");

        return "redirect:/login";
    }


    @ExceptionHandler
    @ResponseBody
    public Info handleException(HttpServletRequest request, HttpServletResponse response, Exception e){
        String servletPath = request.getServletPath();
        Info info = new Info();
        if(servletPath.equals("/api/fileupload/simditor")){
            info.put("success",false);
            info.put("msg","upload fail");
            response.setStatus(203);
        }else{
            response.setStatus(500);
            info.error(e.getMessage());
            info.put("exception type",e.getClass());
        }
        return info;
    }




}
