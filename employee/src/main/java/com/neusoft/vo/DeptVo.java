package com.neusoft.vo;

import java.util.Date;
import java.util.List;

public class DeptVo {
    private int deptId;
    private String deptName;
    private Integer deptLeaderId;
    private String deptLeaderName;
    private int employeeCount;
    private int deptQuota;
    private Date createTime;
    private Integer parentDept;
    private String status;
    private List<DeptEmployeeVo> deptEmployeeVoList;

    public Integer getDeptLeaderId() {
        return deptLeaderId;
    }

    public void setDeptLeaderId(Integer deptLeaderId) {
        this.deptLeaderId = deptLeaderId;
    }

    public String getDeptLeaderName() {
        return deptLeaderName;
    }

    public void setDeptLeaderName(String deptLeaderName) {
        this.deptLeaderName = deptLeaderName;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }
    public int getDeptId() {
        return deptId;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getDeptName() {
        return deptName;
    }
    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }
    public int getEmployeeCount() {
        return employeeCount;
    }
    public void setDeptQuota(int deptQuota) {
        this.deptQuota = deptQuota;
    }
    public int getDeptQuota() {
        return deptQuota;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setParentDept(Integer parentDept) {
        this.parentDept = parentDept;
    }
    public Integer getParentDept() {
        return parentDept;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public List<DeptEmployeeVo> getDeptEmployeeVoList() {
        return deptEmployeeVoList;
    }

    public void setDeptEmployeeVoList(List<DeptEmployeeVo> deptEmployeeVoList) {
        this.deptEmployeeVoList = deptEmployeeVoList;
    }
}
