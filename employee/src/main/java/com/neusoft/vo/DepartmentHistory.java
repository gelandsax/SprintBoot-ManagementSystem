package com.neusoft.vo;

import java.util.Date;

public class DepartmentHistory {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private String old_department;
    private String new_department;
    private Date start_date;
    private Date end_date;

    public void setOld_department(String old_department) {
        this.old_department = old_department;
    }
    public String getOld_department() {
        return old_department;
    }
    public void setNew_department(String new_department) {
        this.new_department = new_department;
    }
    public String getNew_department() {
        return new_department;
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
