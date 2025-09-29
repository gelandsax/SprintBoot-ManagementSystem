package com.neusoft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.dto.Login;
import com.neusoft.dto.UserLogin;
import com.neusoft.mapper.RolesMapper;
import com.neusoft.mapper.UserRolesMapper;
import com.neusoft.mapper.UsersMapper;
import com.neusoft.model.Roles;
import com.neusoft.model.UserRoles;
import com.neusoft.model.Users;
import com.neusoft.service.IUserService;
import com.neusoft.utils.JwtService;
import com.neusoft.utils.UserUtils;
import com.neusoft.vo.User;
import com.neusoft.vo.UserLoginVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    UsersMapper usersMapper;
    @Resource
    JwtService jwtService;
    @Resource
    UserRolesMapper userRolesMapper;
    @Resource
    RolesMapper rolesMapper;

    @Override
    public UserLoginVo login(UserLogin userLogin) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        // 条件1：email 等于 userLogin.getEmail()
        queryWrapper.eq("email", userLogin.getEmail())
                // 或者 条件2：username 等于 userLogin.getUsername()
                .or()
                .eq("username", userLogin.getUsername());
        Users users = usersMapper.selectOne(queryWrapper);
        if(!users.getPassword().equals(userLogin.getPassword())){
            return null;
        }
        if(users != null){
            User user = new User();
            user.setEmail(users.getEmail());
            user.setUsername(users.getUsername());
            user.setEmployeeId(users.getId());

            QueryWrapper<UserRoles> queryUserRolesWrapper = new QueryWrapper<>();
            queryUserRolesWrapper.eq("user_id",users.getId());
            UserRoles userRoles = userRolesMapper.selectOne(queryUserRolesWrapper);

            QueryWrapper<Roles> queryRolesWrapper = new QueryWrapper<>();
            queryRolesWrapper.eq("id",userRoles.getRoleId());
            Roles roles = rolesMapper.selectOne(queryRolesWrapper);

            user.setUserType(roles.getRoleName());

            String token = jwtService.toToken(users);
            user.setToken(token);

            UserLoginVo userLoginVo = new UserLoginVo();
            userLoginVo.setUser(user);
            return userLoginVo;

        }
        else {
            return null;
        }
    }

    @Override
    public Users changePassWord(String oldPass, String newPass) {
        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginUser.getUsername()).eq("password", oldPass);
        Users user = usersMapper.selectOne(queryWrapper);
        if(user == null){
            throw new RuntimeException("密码错误");
        }
        else{
            user.setPassword(newPass);
            user.setStatus("active");
            int count = usersMapper.updateById(user);
            if(count == 1){
                return user;
            }
            else{
                throw new RuntimeException("修改密码失败");
            }
        }
    }

    @Override
    public Users findByEmail(String email) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);

        return usersMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean updatePassword(Integer userId, String newPassword) {
        Users user = new Users();
        user.setId(userId);
        user.setPassword(newPassword);
        user.setStatus("inactive");
        int rows = usersMapper.updateById(user);

        return rows > 0;
    }
}
