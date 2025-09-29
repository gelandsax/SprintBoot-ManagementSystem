package com.neusoft.vo;

import java.sql.Date;
import java.time.LocalDate;

public class VacationReqVo {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;
    private String username;
    private String vocationType;
    private LocalDate startDate;
    private LocalDate endDate;
    private int days;
    private String reason;
    private String status;

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
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public LocalDate getEndDate() {
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
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
