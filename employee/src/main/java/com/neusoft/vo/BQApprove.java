package com.neusoft.vo;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class BQApprove {


    private int id;
    private String username;
    private String status;
    private LocalDate workdate;
    private LocalTime check_in;
    private LocalTime check_out;
    private String reason;

    public LocalDate getWorkdate() {
        return workdate;
    }

    public void setWorkdate(LocalDate workdate) {
        this.workdate = workdate;
    }

    public LocalTime getCheck_in() {
        return check_in;
    }

    public void setCheck_in(LocalTime check_in) {
        this.check_in = check_in;
    }

    public LocalTime getCheck_out() {
        return check_out;
    }

    public void setCheck_out(LocalTime check_out) {
        this.check_out = check_out;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getReason() {
        return reason;
    }
}
