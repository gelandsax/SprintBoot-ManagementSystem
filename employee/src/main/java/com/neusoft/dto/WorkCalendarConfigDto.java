package com.neusoft.dto;

public class WorkCalendarConfigDto {
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay_type() {
        return day_type;
    }

    public void setDay_type(String day_type) {
        this.day_type = day_type;
    }

    private String date;
    private String day_type;
}
