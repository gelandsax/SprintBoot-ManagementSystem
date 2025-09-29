package com.neusoft.bean;

import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {
    // 生成6位随机数字密码
    public static String generatePassword() {
        // 使用String.format确保不足6位时前面补0
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}
