package com.neusoft.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.model.Users;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersMapper extends BaseMapper<Users> {
    List<String> getPermissionsByUserId(@Param("userId") int userId);
}
