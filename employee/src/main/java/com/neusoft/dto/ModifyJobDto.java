package com.neusoft.dto;

public class ModifyJobDto {
    private int employeeId;
    private int newJobId;

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    public int getEmployeeId() {
        return employeeId;
    }

    public int getNewJobId() {
        return newJobId;
    }

    public void setNewJobId(int newJobId) {
        this.newJobId = newJobId;
    }
}
