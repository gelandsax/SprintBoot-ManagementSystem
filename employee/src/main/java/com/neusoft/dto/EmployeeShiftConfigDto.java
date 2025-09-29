package com.neusoft.dto;

public class EmployeeShiftConfigDto {
    private int empId;
    private int workShiftId;

    public void setEmpId(int empId) {
        this.empId = empId;
    }
    public int getEmpId() {
        return empId;
    }
    public void setWorkShiftId(int workShiftId) {
        this.workShiftId = workShiftId;
    }
    public int getWorkShiftId() {
        return workShiftId;
    }
}
