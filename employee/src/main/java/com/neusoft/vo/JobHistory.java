package com.neusoft.vo;

import java.util.Date;

public class JobHistory {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private String old_job;
    private String new_job;
    private Date start_date;
    private Date end_date;

    public void setOld_job(String old_job) {
        this.old_job = old_job;
    }
    public String getOld_job() {
        return old_job;
    }
    public void setNew_job(String new_job) {
        this.new_job = new_job;
    }
    public String getNew_job() {
        return new_job;
    }
    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }
    public Date getStart_date() {
        return start_date;
    }
    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }
    public Date getEnd_date() {
        return end_date;
    }
}
