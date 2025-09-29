package com.neusoft.vo;

import java.util.List;

public class JobVo {
    private Integer jobId;
    private String jobName;
    private Integer jobLevel;
    private String status;
    private List<JobEmployeeVo> jobEmployeeVoList;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(Integer jobLevel) {
        this.jobLevel = jobLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<JobEmployeeVo> getJobEmployeeVoList() {
        return jobEmployeeVoList;
    }

    public void setJobEmployeeVoList(List<JobEmployeeVo> jobEmployeeVoList) {
        this.jobEmployeeVoList = jobEmployeeVoList;
    }
}
