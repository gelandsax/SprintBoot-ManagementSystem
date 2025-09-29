package com.neusoft.vo;

import java.sql.Time;
import java.time.LocalDateTime;

public class ClockVo {
    private String username;

    public Time getCheck_in() {
        return check_in;
    }

    public void setCheck_in(Time check_in) {
        this.check_in = check_in;
    }

    public Time getCheck_out() {
        return check_out;
    }

    public void setCheck_out(Time check_out) {
        this.check_out = check_out;
    }

    private Time check_in;
    private Time check_out;
    private String status;

    public LocalDateTime getServerTime(LocalDateTime now) {
        return ServerTime;
    }

    public void setServerTime(LocalDateTime serverTime) {
        ServerTime = serverTime;
    }

    private LocalDateTime ServerTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
