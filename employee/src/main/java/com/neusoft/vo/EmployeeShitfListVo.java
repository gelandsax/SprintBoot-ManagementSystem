package com.neusoft.vo;

import com.neusoft.model.Employees;

import java.util.List;

public class EmployeeShitfListVo {
    private List<EmployeeShiftVo> employees;

    public List<EmployeeShiftVo> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeShiftVo> employees) {
        this.employees = employees;
    }
}
