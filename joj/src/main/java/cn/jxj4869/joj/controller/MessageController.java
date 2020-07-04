package cn.jxj4869.joj.controller;


import cn.jxj4869.joj.entity.Message;
import cn.jxj4869.joj.mapper.MessageMapper;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.service.IMessageService;
import org.apache.ibatis.annotations.Delete;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    IMessageService messageService;

    @GetMapping({"/", ""})
    public String index(Model model) {
//        MyPage<Message> myPage = messageService.messagePage(1);
//        model.addAttribute("page",myPage);
//        List<Message> comments = myPage.getRecords();
//        model.addAttribute("messages",comments);
        return "message/message";
    }


    @RequestMapping("/list/{page}")
    public String listPage(@PathVariable("page") Integer page, Model model) {
        MyPage<Message> myPage = messageService.messagePage(page);
        model.addAttribute("page",myPage);
        List<Message> comments = myPage.getRecords();
        model.addAttribute("messages",comments);
        return "message/message::messageList";
    }


    @PostMapping("/")
    @ResponseBody
    public Info post(Message message) {
         return messageService.messageSave(message);
    }

    @DeleteMapping("/{mid}")
    @ResponseBody
    @RequiresAuthentication
    public Info delete(@PathVariable("mid") Integer mid){
        Info info = messageService.messageDelete(mid);
        return info;
    }

}
