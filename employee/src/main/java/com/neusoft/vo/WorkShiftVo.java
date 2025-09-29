package com.neusoft.vo;

import javax.xml.crypto.Data;
import java.sql.Time;

public class WorkShiftVo {
    private String shiftName;
    private Time startTime;
    private Time endTime;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }
    public String getShiftName() {
        return shiftName;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
