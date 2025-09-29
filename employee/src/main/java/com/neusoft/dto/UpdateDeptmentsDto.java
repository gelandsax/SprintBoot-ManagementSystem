package com.neusoft.dto;

public class UpdateDeptmentsDto {
    private String deptName;
    private Integer leaderId;
    private Integer deptSize;
    private String fatherDeptName;

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getDeptName() {
        return deptName;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public void setDeptSize(Integer deptSize) {
        this.deptSize = deptSize;
    }
    public Integer getDeptSize() {
        return deptSize;
    }
    public void setFatherDeptName(String fatherDeptName) {
        this.fatherDeptName = fatherDeptName;
    }
    public String getFatherDeptName() {
        return fatherDeptName;
    }
}
