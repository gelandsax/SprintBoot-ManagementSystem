package com.neusoft.vo;

import java.util.Date;
import java.util.List;

public class Profile {
    private Integer id;
    private String salary;
    private String permission;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    private String user_name;

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    private String emp_name;
    private String email;
    private String gender;
    private Date birthday;
    private String maritalStatus;
    private Date firstWorkDate;
    private Date hireDate;
    private String department;
    private String job;
    private List<DepartmentHistory> departmentHistory;
    private List<JobHistory> jobHistory;


    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
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
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
    public String getMaritalStatus() {
        return maritalStatus;
    }
    public void setFirstWorkDate(Date firstWorkDate) {
        this.firstWorkDate = firstWorkDate;
    }
    public Date getFirstWorkDate() {
        return firstWorkDate;
    }
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
    public Date getHireDate() {
        return hireDate;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getDepartment() {
        return department;
    }
    public void setJob(String job) {
        this.job = job;
    }
    public String getJob() {
        return job;
    }
    public void setDepartmentHistory(List<DepartmentHistory> departmentHistory) {
        this.departmentHistory = departmentHistory;
    }
    public List<DepartmentHistory> getDepartmentHistory() {
        return departmentHistory;
    }
    public void setJobHistory(List<JobHistory> jobHistory) {
        this.jobHistory = jobHistory;
    }
    public List<JobHistory> getJobHistory() {
        return jobHistory;
    }
}
