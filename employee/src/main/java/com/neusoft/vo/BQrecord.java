package com.neusoft.vo;


import java.sql.Time;
import java.util.Date;

public class BQrecord {
    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    private Integer attendanceId;
    private String username;
    private String status;
    private Date workdate;
    private Time check_in;
    private Time check_out;

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
    public void setWorkdate(Date workdate) {
        this.workdate = workdate;
    }
    public Date getWorkdate() {
        return workdate;
    }
    public void setCheck_in(Time check_in) {
        this.check_in = check_in;
    }
    public Time getCheck_in() {
        return check_in;
    }
    public void setCheck_out(Time check_out) {
        this.check_out = check_out;
    }
    public Time getCheck_out() {
        return check_out;
    }
}
