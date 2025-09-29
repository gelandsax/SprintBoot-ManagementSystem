package com.neusoft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.dto.EmployeeShiftConfigDto;
import com.neusoft.mapper.EmployeeShiftsMapper;
import com.neusoft.mapper.EmployeesMapper;
import com.neusoft.model.EmployeeShifts;
import com.neusoft.model.Employees;
import com.neusoft.service.IEmployeesService;
import com.neusoft.vo.EmployeeInfo;
import com.neusoft.vo.EmplyeesListVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeesServiceImpl implements IEmployeesService {
    @Resource
    EmployeesMapper employeesMapper;
    @Resource
    EmployeeShiftsMapper employeeShiftsMapper;
    public EmplyeesListVo getEmployeesByDepartment(int dept_id){
        List<Employees> list = employeesMapper.findAllEmployeeByDepartment(dept_id);
        List<EmployeeInfo> employeeInfoList = new ArrayList<>();
        for (Employees employees : list) {
            EmployeeInfo employeeInfo = new EmployeeInfo();
            employeeInfo.setEmployeeId(employees.getId());
            employeeInfo.setEmployeeName(employees.getName());
            employeeInfo.setDeptId(employees.getDeptId());
            employeeInfoList.add(employeeInfo);
        }
        EmplyeesListVo emplyeesListVo = new EmplyeesListVo();
        emplyeesListVo.setEmployees(employeeInfoList);
        return emplyeesListVo;
    }
    @Override
    public int configEmployeeShift(EmployeeShiftConfigDto employeeShiftConfigDto){
        int empid = employeeShiftConfigDto.getEmpId();
        int work_shift_id = employeeShiftConfigDto.getWorkShiftId();
        QueryWrapper<EmployeeShifts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("emp_id", empid);
        EmployeeShifts employeeShifts = employeeShiftsMapper.selectOne(queryWrapper);
        employeeShifts.setShiftId(work_shift_id);
        employeeShifts.setEffectiveDate( java.sql.Date.valueOf(LocalDate.now()));
        return employeeShiftsMapper.updateById(employeeShifts);
    }
}
