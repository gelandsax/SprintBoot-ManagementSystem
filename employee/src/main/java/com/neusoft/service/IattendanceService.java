package com.neusoft.service;

import com.neusoft.dto.BQProcessDto;
import com.neusoft.dto.BQApplay;
import com.neusoft.vo.*;

public interface IattendanceService {
    ServerTimeVo getServerTime();
    ClockVo getClockRecord();
    ClockVo getClock(String username,String type);
    BQListVo getBQList();
    int BQApplay(BQApplay applay);
    BQApprovListVo getAllBQApprovList();
    int BQAgree(BQProcessDto bqAgreeDto);
    int BQAgreeAll(BQProcessDto bqAgreeDto);
    int BQReject(BQProcessDto bqAgreeDto);
    int BQRejectAll(BQProcessDto bqAgreeDto);
    AttendanceRecordsVo getAttendanceRecords(int month);
    CheckInCountVo CheckInCount();
}
