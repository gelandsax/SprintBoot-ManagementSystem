package com.neusoft.vo;

public class EmployeeShiftVo {
    private int empId;
    private String empname;
    private String job;
    private String dept;
    private String work_shift;

    public void setEmpId(int empId) {
        this.empId = empId;
    }
    public int getEmpId() {
        return empId;
    }
    public void setEmpname(String empname) {
        this.empname = empname;
    }
    public String getEmpname() {
        return empname;
    }
    public void setJob(String job) {
        this.job = job;
    }
    public String getJob() {
        return job;
    }
    public void setDept(String dept) {
        this.dept = dept;
    }
    public String getDept() {
        return dept;
    }
    public void setWork_shift(String work_shift) {
        this.work_shift = work_shift;
    }
    public String getWork_shift() {
        return work_shift;
    }
}
