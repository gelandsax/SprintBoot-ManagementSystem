package com.neusoft.vo;

import java.sql.Date;

public class WorkCalendarDayType {
    private Date date;
    private String dayType;

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }
    public void setDayType(String dayType) {
        this.dayType = dayType;
    }
    public String getDayType() {
        return dayType;
    }
}
