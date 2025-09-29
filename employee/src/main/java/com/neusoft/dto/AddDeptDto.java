package com.neusoft.dto;

public class AddDeptDto {
    private Integer deptId;
    private String deptName;
    private Integer leaderId;
    private Integer deptSize;
    private String fatherDeptName;

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        leaderId = leaderId;
    }

    public Integer getDeptSize() {
        return deptSize;
    }

    public void setDeptSize(Integer deptSize) {
        this.deptSize = deptSize;
    }

    public String getFatherDeptName() {
        return fatherDeptName;
    }

    public void setFatherDeptName(String fatherDeptName) {
        this.fatherDeptName = fatherDeptName;
    }
}
