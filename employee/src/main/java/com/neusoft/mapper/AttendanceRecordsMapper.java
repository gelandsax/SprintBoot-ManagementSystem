package com.neusoft.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.model.AttendanceRecords;
import com.neusoft.vo.AttendanceRecord;
import com.neusoft.vo.AttendanceRecordsVo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRecordsMapper extends BaseMapper<AttendanceRecords> {
    List<AttendanceRecord> getAttendanceCalendar(
            @Param("employeeId") int employeeId,
            @Param("year") int year,
            @Param("month") int month,
            @Param("today") LocalDate today);
}
