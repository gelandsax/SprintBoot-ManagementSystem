package com.neusoft.dto;

import java.time.LocalDate;
import java.util.Date;

public class Employee {
    private String name;
    private String gender;
    private Date birthday;
    private Date hireDate;
    private Date firstWorkDate;
    private String salary;
    private String maritalStatus;
    private int deptId;
    private int jobId;
    private String permisson;
    private String workShifts;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getGender() {
        return gender;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public Date getBirthday() {
        return birthday;
    }
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public void setFirstWorkDate(Date firstWorkDate) {
        this.firstWorkDate = firstWorkDate;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public Date getFirstWorkDate() {
        return firstWorkDate;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
    public String getMaritalStatus() {
        return maritalStatus;
    }
    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }
    public int getDeptId() {
        return deptId;
    }
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
    public int getJobId() {
        return jobId;
    }
    public void setPermisson(String permisson) {
        this.permisson = permisson;
    }
    public String getPermisson() {
        return permisson;
    }
    public void setWorkShifts(String workShifts) {
        this.workShifts = workShifts;
    }
    public String getWorkShifts() {
        return workShifts;
    }
}
