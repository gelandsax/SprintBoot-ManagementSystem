package com.neusoft.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.dto.EmployeeShiftInfoDto;
import com.neusoft.model.Employees;
import com.neusoft.vo.AttendEmployeeInfo;
import com.neusoft.vo.EmployeeShiftVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeesMapper extends BaseMapper<Employees> {
    List<Employees> findAll();
    List<Employees> findAllEmployeeByDepartment(@Param("dept_id") int departmentId);
    List<EmployeeShiftVo> findAllEmployeeShift();
    List<AttendEmployeeInfo> findAllAttendEmployeeInfo();
}
