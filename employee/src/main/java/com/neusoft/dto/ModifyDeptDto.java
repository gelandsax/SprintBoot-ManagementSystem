package com.neusoft.dto;

public class ModifyDeptDto {
    private int employeeId;
    private int newDepartmentId;

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    public int getEmployeeId() {
        return employeeId;
    }
    public void setNewDepartmentId(int newDepartmentId) {
        this.newDepartmentId = newDepartmentId;
    }
    public int getNewDepartmentId() {
        return newDepartmentId;
    }
}
