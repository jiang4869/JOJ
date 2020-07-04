package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.pojo.Email;
import cn.jxj4869.joj.service.IEmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author JiangXiaoju
 * @date 2020-06-12 21:01
 */
@Service
public class EmailServiceImpl implements IEmailService {


    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    @RabbitListener(queues = {"email"})
    public void receive(Email email) throws InterruptedException, MessagingException {


        MimeMessage mimeMailMessage = this.mailSender.createMimeMessage();

        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMailMessage, true, "utf-8");
        messageHelper.setFrom(from);
        messageHelper.setTo(email.getTo());
        messageHelper.setSubject(email.getSubject());
        messageHelper.setText(email.getText(), true);
        mailSender.send(mimeMailMessage);

        Thread.sleep(1000 * 10);

    }
}
