package cn.jxj4869.joj.controller.admin;

import cn.jxj4869.joj.entity.Message;
import cn.jxj4869.joj.entity.User;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.service.IMessageService;
import cn.jxj4869.joj.utils.Constants;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-30 16:31
 */
@Controller
@RequestMapping("/admin/message")
@RequiresRoles("admin")
public class AdminMessageController {

    @Autowired
    private IMessageService messageService;


    @RequestMapping({"", "/list/{page}"})
    public String messageList(@PathVariable(value = "page", required = false) Integer page, Model model) {
        if (ObjectUtils.isEmpty(page)) {
            page = Integer.valueOf(1);
        }
        MyPage<Message> myPage=new MyPage<>(page, Constants.DEFAULT_ADMIN_PAGE_SIZE);
        MyPage<Message> messageMyPage = messageService.page(myPage);
        messageMyPage.setShowBtnNum();
        List<Message> messageList = messageMyPage.getRecords();

        model.addAttribute("messageList",messageList);
        model.addAttribute("page",messageMyPage);

        return "admin/messageList";
    }


    @DeleteMapping("/{mid}")
    @ResponseBody
    @Transactional
    public Info userDelete(@PathVariable("mid") Serializable mid) throws Exception {
        boolean flag = messageService.removeById(mid);
        Info info = new Info();
        if (!flag){
            info.error("fail");
            throw new Exception("fail");
        }else{
            info.ok("success");
        }
        return info;
    }
}
