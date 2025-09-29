package com.neusoft.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public class BQApplay {
    private Integer attendanceId;
    private String username;
    private String status;
    private String workdate;   // yyyy-MM-dd
    private String checkIn;    // HH:mm:ss
    private String checkOut;   // HH:mm:ss
    private String reason;

    // getter & setter

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getWorkdate() { return workdate; }
    public void setWorkdate(String workdate) { this.workdate = workdate; }
    public String getCheckIn() { return checkIn; }
    public void setCheckIn(String checkIn) { this.checkIn = checkIn; }
    public String getCheckOut() { return checkOut; }
    public void setCheckOut(String checkOut) { this.checkOut = checkOut; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}


