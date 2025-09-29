package com.neusoft.service;

import com.neusoft.dto.Login;
import com.neusoft.dto.UserLogin;
import com.neusoft.model.Users;
import com.neusoft.vo.UserLoginVo;

public interface IUserService {
    UserLoginVo login(UserLogin userLogin);
    Users changePassWord(String oldPass, String newPass);
    // 根据邮箱查询用户（新增）
    Users findByEmail(String email);

    // 更新用户密码（新增）
    boolean updatePassword(Integer userId, String newPassword);
}
