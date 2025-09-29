package com.neusoft.dto;

import java.util.List;

public class BQProcessDto {
    private List<Integer> attendanceRetroactiveIds; // 多个申请记录id


    public List<Integer> getAttendanceRetroactiveIds() {
        return attendanceRetroactiveIds;
    }

    public void setAttendanceRetroactiveIds(List<Integer> attendanceRetroactiveIds) {
        this.attendanceRetroactiveIds = attendanceRetroactiveIds;
    }

}

