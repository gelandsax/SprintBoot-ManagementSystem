package com.neusoft.vo;

import java.util.List;

public class AttendanceRecordsVo {
    private int month;
    private int days;
    private List<AttendanceRecord> records;

    public void setMonth(int month) {
        this.month = month;
    }
    public int getMonth() {
        return month;
    }
    public void setDays(int days) {
        this.days = days;
    }
    public int getDays() {
        return days;
    }
    public void setAttendanceRecord(List<AttendanceRecord> records) {
        this.records = records;
    }
    public List<AttendanceRecord> getAttendanceRecord() {
        return records;
    }
}
