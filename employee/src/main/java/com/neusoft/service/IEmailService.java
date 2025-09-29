package com.neusoft.service;

public interface IEmailService {
    /**
     * 发送邮件
     * @param toEmail 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendEmail(String toEmail, String subject, String content);
}
