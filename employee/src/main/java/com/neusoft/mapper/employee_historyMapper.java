package com.neusoft.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.model.EmployeeHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface employee_historyMapper extends BaseMapper<EmployeeHistory> {
   List<EmployeeHistory> getJobsHistoryByEmpId(@Param("empId") int empId);
   List<EmployeeHistory> getDeptHistoryByEmpId(@Param("empId") int empId);
}
