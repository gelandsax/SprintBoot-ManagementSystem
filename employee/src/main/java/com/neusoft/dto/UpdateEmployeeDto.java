package com.neusoft.dto;

import java.util.Date;

public class UpdateEmployeeDto {
    private Integer employeeId;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    private String employeeName;
    private String loginName;
    private String gender;
    private Date birthday;
    private Date hiredDate;
    private String salary;
    private String permission;

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public String getEmployeeName() {
        return employeeName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getLoginName() {
        return loginName;
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
    public void setHiredDate(Date hiredDate) {
        this.hiredDate = hiredDate;
    }
    public Date getHiredDate() {
        return hiredDate;
    }
    public void setSalary(String salary) {
        this.salary = salary;
    }
    public String getSalary() {
        return salary;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }
    public String getPermission() {
        return permission;
    }
}
