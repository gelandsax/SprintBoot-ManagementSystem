package com.neusoft.vo;

import com.neusoft.model.WorkCalendar;

import java.util.List;

public class WorkCalendarVo {
    private List<WorkCalendarDayType> WorkCalendar;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    private int days;
    private int year;
    public void setWorkCalendar(List<WorkCalendarDayType> WorkCalendar) {
        this.WorkCalendar = WorkCalendar;
    }
    public List<WorkCalendarDayType> getWorkCalendar() {
        return WorkCalendar;
    }
}
