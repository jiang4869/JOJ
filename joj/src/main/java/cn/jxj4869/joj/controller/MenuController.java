package cn.jxj4869.joj.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MenuController {

    @RequestMapping("/register")
    public String register() {
        return "user/register";
    }

    @RequestMapping("/login")
    public String login() {
        return "user/login";
    }



    @GetMapping("/findPassword")
    public String findPasswordPage(String userName, String confirmCode, Model model){
        model.addAttribute("userName",userName);
        model.addAttribute("confirmCode",confirmCode);
        return "user/findPassword";
    }


    @RequestMapping({"/", "index"})
    public String index() {
        return "index";
    }

}
