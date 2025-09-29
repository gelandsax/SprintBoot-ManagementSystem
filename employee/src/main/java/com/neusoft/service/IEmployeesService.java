package com.neusoft.service;

import com.neusoft.dto.EmployeeShiftConfigDto;
import com.neusoft.vo.EmplyeesListVo;

public interface IEmployeesService {
    EmplyeesListVo getEmployeesByDepartment(int dept_id);
    int configEmployeeShift(EmployeeShiftConfigDto employeeShiftConfigDto);
}
