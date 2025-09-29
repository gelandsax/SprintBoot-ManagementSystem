package com.neusoft.vo;

import com.neusoft.model.Employees;

import java.util.List;

public class CheckInCountVo {
    private int attendanceCount;
    private List<AttendEmployeeInfo> AttendEmployeeInfoList;

    public int getAttendanceCount() {
        return attendanceCount;
    }

    public void setAttendanceCount(int attendanceCount) {
        this.attendanceCount = attendanceCount;
    }

    public List<AttendEmployeeInfo> getAttendEmployeeInfoList() {
        return AttendEmployeeInfoList;
    }

    public void setAttendEmployeeInfoList(List<AttendEmployeeInfo> attendEmployeeInfoList) {
        AttendEmployeeInfoList = attendEmployeeInfoList;
    }
}
