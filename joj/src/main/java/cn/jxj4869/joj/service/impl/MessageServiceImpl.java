package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.Message;
import cn.jxj4869.joj.mapper.MessageMapper;
import cn.jxj4869.joj.pojo.Email;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.service.IMessageService;
import cn.jxj4869.joj.utils.Constants;

import cn.jxj4869.joj.utils.Tools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.rmi.CORBA.Util;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    @Autowired
    MessageMapper messageMapper;


    @Autowired
    RabbitTemplate rabbitTemplate;

    @Async
    public void replyRemind(Message message) {
        String content = "Your message has been answered, go check it out soon";
        Email email = new Email();
        email.setText(content);
        email.setTo(message.getEmail());
        email.setSubject("Reply remind");
        rabbitTemplate.convertAndSend("joj.email","email",email);
    }


    @Override
    public MyPage<Message> messagePage(Integer currentPage) {
        MyPage<Message> page = new MyPage<>(currentPage, Constants.DEFAULT_PAGE_SIZE);
        MyPage<Message> myPage = messageMapper.selectParentMessageNullPage(page);
        myPage.setShowBtnNum();
        return myPage;
    }


    @Override
    public MyPage<Message> messageAllPage(Integer currentPage) {
        MyPage<Message> page = new MyPage<>(currentPage, Constants.DEFAULT_PAGE_SIZE);
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("createTime");
        MyPage<Message> messageMyPage = messageMapper.selectPage(page, wrapper);
        messageMyPage.setShowBtnNum();
        return messageMyPage;
    }


    @Override
    @Transactional
    public Info messageDelete(Integer mid) {
        Info info = new Info();
        int cnt = messageMapper.deleteById(mid);
        if (cnt == 1) {
            info.ok("successfully deleted");
        } else {
            info.error("failed to delete");
        }
        return info;
    }

    @Override
    @Transactional
    public Info messageSave(Message message) {
        Info info = new Info();

        message.setCreateTime(new Date());
        if (message.getParentMessageId().equals(-1)) {
            message.setParentMessageId(null);
        } else {
            Message parentMessage = messageMapper.selectById(message.getParentMessageId());
            if (parentMessage.getRemind()) {
                replyRemind(parentMessage);
            }
        }

        if (message.getReplyMessageId().equals(-1)) {
            message.setReplyMessageId(null);
        }

        if (message.getUid().equals(-1)) {
            message.setUid(null);
            message.setAvatar(Tools.getAvatar());
        }

        int cnt = messageMapper.insert(message);

        if (cnt == 1) {
           info.ok("Saved successfully");
        } else {
           info.error("Save failed");
        }

        return info;
    }

}
