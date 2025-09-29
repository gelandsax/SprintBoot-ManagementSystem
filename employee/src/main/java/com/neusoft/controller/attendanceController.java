package com.neusoft.controller;

import com.neusoft.dto.AttendanceRecordDto;
import com.neusoft.dto.BQProcessDto;
import com.neusoft.dto.BQApplayDto;
import com.neusoft.dto.ClockDto;
import com.neusoft.response.ApiResponse;
import com.neusoft.service.IattendanceService;
import com.neusoft.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class attendanceController {
    @Autowired
    IattendanceService attendanceService;
    @GetMapping("/attendance/serverTime")
    public ApiResponse<ServerTimeVo> getServerTime() {
        return ApiResponse.success(attendanceService.getServerTime());
    }
    @GetMapping("/attendance/record")
    public ApiResponse<ClockVo> getClockRecord() {
        return ApiResponse.success(attendanceService.getClockRecord());
    }
    @PostMapping("/attendance/clock")
    public ApiResponse<ClockVo> getClock(@RequestBody ClockDto clockDto) {
        ClockVo clockVo = attendanceService.getClock(clockDto.getUsername(),clockDto.getType());
        return ApiResponse.success(clockVo);
    }

    @GetMapping("/attendance/BQAttend")
    public ApiResponse<BQListVo> getAllBQAttend() {
        BQListVo bqListVo = attendanceService.getBQList();
        return ApiResponse.success(bqListVo);
    }

    @PostMapping("/attendance/BQApply")
    public ApiResponse applyBQ(@RequestBody BQApplayDto bqApplayDto) {
        if(attendanceService.BQApplay(bqApplayDto.getBQApplay()) == 1){
            return ApiResponse.success();
        }
        else{
            throw new RuntimeException("提交请求失败");
        }
    }

    @GetMapping("/attendance/BQApprove")
    public ApiResponse<BQApprovListVo> getAllBQApprove() {
        BQApprovListVo bqApprovListVo = attendanceService.getAllBQApprovList();
        return ApiResponse.success(bqApprovListVo);
    }

    @PostMapping("/attendance/BQApprove/agree")
    public ApiResponse agree(@RequestBody BQProcessDto bqAgreeDto) {
        int count = attendanceService.BQAgree(bqAgreeDto);
        if(count == 1){
            return ApiResponse.success();
        }else{
            return ApiResponse.error(500,"更新失败");
        }
    }

    @PostMapping("/attendance/BQApprove/reject")
    public ApiResponse reject(@RequestBody BQProcessDto bqRejectDto) {
        int count = attendanceService.BQAgree(bqRejectDto);
        if(count == 1){
            return ApiResponse.success();
        }else{
            return ApiResponse.error(500,"更新失败");
        }
    }

    @PostMapping("/attendance/BQApprove/agreeAll")
    public ApiResponse agreeAll(@RequestBody BQProcessDto bqAgreeDto) {
        int count = attendanceService.BQAgree(bqAgreeDto);
        if(count == bqAgreeDto.getAttendanceRetroactiveIds().size()){
            return ApiResponse.success();
        }else{
            return ApiResponse.error(500,"更新失败");
        }
    }

    @PostMapping("/attendance/BQApprove/rejectAll")
    public ApiResponse rejectAll(@RequestBody BQProcessDto bqRejectDto) {
        int count = attendanceService.BQAgree(bqRejectDto);
        if(count == bqRejectDto.getAttendanceRetroactiveIds().size()){
            return ApiResponse.success();
        }else{
            return ApiResponse.error(500,"更新失败");
        }
    }

    @GetMapping("/attendanceRecords/{month}")
    public ApiResponse<AttendanceRecordsVo> getAttendanceRecords(@PathVariable int month) {
        AttendanceRecordsVo attendanceRecords= attendanceService.getAttendanceRecords(month);
        return ApiResponse.success(attendanceRecords);

    }

    @GetMapping("/attendance/CheckInCount")
    public ApiResponse<CheckInCountVo> checkInCount() {
        return ApiResponse.success(attendanceService.CheckInCount());
    }

}
