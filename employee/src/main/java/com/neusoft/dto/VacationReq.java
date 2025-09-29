package com.neusoft.dto;

import java.sql.Date;

public class VacationReq {
    private String username;
    private String vocationType;
    private String startDate;
    private String endDate;
    private int days;
    private String reason;

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setVocationType(String vocationType) {
        this.vocationType = vocationType;
    }
    public String getVocationType() {
        return vocationType;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setDays(int days) {
        this.days = days;
    }
    public int getDays() {
        return days;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getReason() {
        return reason;
    }
}
