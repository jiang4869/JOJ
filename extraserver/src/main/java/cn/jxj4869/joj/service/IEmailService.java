package cn.jxj4869.joj.service;


import cn.jxj4869.joj.pojo.Email;

import javax.mail.MessagingException;


public interface IEmailService {

    public void receive(Email email) throws InterruptedException, MessagingException;
}
