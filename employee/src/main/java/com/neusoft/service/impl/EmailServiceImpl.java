package com.neusoft.service.impl;

import com.neusoft.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void sendEmail(String toEmail, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("3225916474@qq.com"); // 显式设置发件人（与配置一致）
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(content);
            System.out.println("开始发送邮件到：" + toEmail);
            mailSender.send(message);
            System.out.println("邮件发送成功！");
        } catch (Exception e) {
            // 打印完整异常信息，方便排查
            System.out.println("邮件发送失败！原因：");
            e.printStackTrace();
            throw new RuntimeException("邮件发送失败：" + e.getMessage());
        }
    }
}
