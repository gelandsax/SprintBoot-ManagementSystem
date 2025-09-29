package com.neusoft.service;

import com.neusoft.dto.AddEmployeeDto;
import com.neusoft.dto.ModifyDeptDto;
import com.neusoft.dto.ModifyJobDto;
import com.neusoft.dto.UpdateEmployeeDto;
import com.neusoft.model.*;
import com.neusoft.vo.Profile;
import com.neusoft.vo.ProfileVo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IProfileService {
    List<WorkShifts> getAllWorkShift();
    List<Departments> getAllDept();
    List<Jobs> getAllJob();
    Profile getProfileByUserId(String username);
    List<ProfileVo> getAllProfiles();
    // 新增员工
    boolean addEmployee(AddEmployeeDto addEmployeeDto);
    // 校验登录名是否存在
    boolean isUsernameExists(String username);
    // 校验邮箱是否存在
    boolean isEmailExists(String email);
    // 校验入职日期是否不早于初次参加工作日期
    boolean isEntryDateValid(Date hireDate, Date firstWorkDate);
    // 更新员工信息
    boolean updateEmployee(UpdateEmployeeDto updateEmployeeDto, Integer id);
    boolean statusUpdate(Integer id);
    boolean modifyDept(ModifyDeptDto modifyDeptDto, Integer id);
    boolean modifyJob(ModifyJobDto modifyJobDto, Integer id);

}
