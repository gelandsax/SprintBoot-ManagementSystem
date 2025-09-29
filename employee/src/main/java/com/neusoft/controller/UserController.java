package com.neusoft.controller;

import com.neusoft.bean.PasswordGenerator;
import com.neusoft.dto.EmailDto;
import com.neusoft.dto.Login;
import com.neusoft.dto.changePasswordDTO;
import com.neusoft.model.Users;
import com.neusoft.response.ApiResponse;
import com.neusoft.service.IEmailService;
import com.neusoft.service.IUserService;
import com.neusoft.utils.UserUtils;
import com.neusoft.vo.UserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    @Autowired
    IUserService iUserService;
    @Autowired
    IEmailService iEmailService;

    @PostMapping("/api/users/login")
    public ApiResponse<UserLoginVo> loginUser(@RequestBody Login reg){
        UserLoginVo userLoginVo = iUserService.login(reg.getUser());
        return ApiResponse.success(userLoginVo);
    }

    @PutMapping("/api/changePassWord")
    public ApiResponse changePassWord(@RequestBody changePasswordDTO changePasswordDTO){
        String oldPass = changePasswordDTO.getOldPassword();
        String newPass = changePasswordDTO.getNewPassword();
        Users user = iUserService.changePassWord(oldPass, newPass);
        if(user != null){
            return ApiResponse.success();
        }
        else{
            return ApiResponse.error(500,"修改失败");
        }
    }

    // 忘记密码接口
    @PostMapping("/api/forgotPassword")
    public ApiResponse forgotPassword(@RequestBody EmailDto emailDto) {
        String email = emailDto.getEmail();

        // 1. 验证邮箱是否存在
        Users user = iUserService.findByEmail(email);
        if (user == null) {
            return ApiResponse.error(404, "该邮箱未注册");
        }

        // 2. 生成6位数字随机密码
        String newPassword = PasswordGenerator.generatePassword();

        // 3. 更新数据库密码
        boolean updateSuccess = iUserService.updatePassword(user.getId(), newPassword);
        if (!updateSuccess) {
            return ApiResponse.error(500, "密码重置失败");
        }

        // 4. 发送包含新密码的邮件
        try {
            iEmailService.sendEmail(
                    email,
                    "密码重置通知",
                    "您的新密码为：" + newPassword + "\n请登录后及时修改密码以保障安全"
            );
            return ApiResponse.success("新密码已发送至您的邮箱");
        } catch (Exception e) {
            // 邮件发送失败时，可选择回滚密码更新（根据业务需求）
            // userService.updatePassword(user.getId(), oldPassword);
            return ApiResponse.error(500, "密码已重置，但邮件发送失败，请联系管理员");
        }
    }

}
