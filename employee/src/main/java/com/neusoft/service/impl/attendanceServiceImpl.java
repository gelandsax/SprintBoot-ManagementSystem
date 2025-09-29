package com.neusoft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.dto.BQProcessDto;
import com.neusoft.dto.BQApplay;
import com.neusoft.mapper.*;
import com.neusoft.model.*;
import com.neusoft.service.IWorkCalendarService;
import com.neusoft.service.IattendanceService;
import com.neusoft.utils.UserUtils;
import com.neusoft.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class attendanceServiceImpl implements IattendanceService {
    @Resource
    WorkShiftsMapper workShiftsMapper;
    @Resource
    AttendanceRecordsMapper attendanceRecordsMapper;
    @Resource
    EmployeeShiftsMapper employeeShiftsMapper;
    @Resource
    UsersMapper usersMapper;
    @Resource
    IWorkCalendarService workCalendarService;

    @Override
    public ServerTimeVo getServerTime(){
        ServerTime serverTime = new ServerTime();
        LocalDateTime localDateTime = LocalDateTime.now();
        serverTime.setYear(localDateTime.getYear());
        serverTime.setMonth(localDateTime.getMonthValue());
        serverTime.setDay(localDateTime.getDayOfMonth());
        serverTime.setHour(localDateTime.getHour());
        serverTime.setMinute(localDateTime.getMinute());
        serverTime.setSecond(localDateTime.getSecond());
        ServerTimeVo serverTimeVo = new ServerTimeVo();
        serverTimeVo.setServerTime(serverTime);
        return serverTimeVo;
    }
    @Override
    public ClockVo getClockRecord(){
        ClockVo clockVo = new ClockVo();
        LocalDate localDate = LocalDate.now();
        Date today = Date.valueOf(localDate);
        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();
        int emplyeeId = loginUser.getEmployeeId();
        QueryWrapper<AttendanceRecords> attendanceRecordsQueryWrapper = new QueryWrapper<>();
        attendanceRecordsQueryWrapper.eq("employee_id", emplyeeId).eq("work_date", today);
        AttendanceRecords records = attendanceRecordsMapper.selectOne(attendanceRecordsQueryWrapper);
        clockVo.setUsername(loginUser.getUsername());
        clockVo.setCheck_in(records.getCheckIn());
        clockVo.setCheck_out(records.getCheckOut());
        clockVo.setStatus(records.getStatus());
        clockVo.getServerTime(LocalDateTime.now());
        return clockVo;
    }
    @Override
    public ClockVo getClock(String username, String type) {

        // 1. 查询用户和班次
        Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("username", username));
        int employeeId = user.getEmployeeId();

        EmployeeShifts employeeShifts = employeeShiftsMapper.selectOne(
                new QueryWrapper<EmployeeShifts>().eq("employee_id", employeeId));
        int shiftId = employeeShifts.getShiftId();

        WorkShifts workShifts = workShiftsMapper.selectOne(
                new QueryWrapper<WorkShifts>().eq("id", shiftId));
        LocalTime startTime = workShifts.getStartTime().toLocalTime();
        LocalTime endTime = workShifts.getEndTime().toLocalTime();

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalTime currentTime = now.toLocalTime();

        // 2. 查询当天考勤记录（假设凌晨6点已初始化好）
        AttendanceRecords record = attendanceRecordsMapper.selectOne(
                new QueryWrapper<AttendanceRecords>()
                        .eq("employee_id", employeeId)
                        .eq("shift_id", shiftId)
                        .eq("work_date", today)
        );

        if (record == null) {
            throw new RuntimeException("当天考勤记录未初始化，请联系管理员");
        }

        // 3. 上班打卡逻辑
        if (type.equals("check_in")) {
            if (record.getCheckIn() != null) {
                throw new RuntimeException("禁止重复打卡上班");
            }

            record.setCheckIn(java.sql.Time.valueOf(currentTime));
            record.setStatus(currentTime.isAfter(startTime) ? "迟到" : "正常");
            attendanceRecordsMapper.updateById(record);

        }
        // 4. 下班打卡逻辑
        else if (type.equals("check_out")) {
            if (record.getCheckOut() != null) {
                long minutes = ChronoUnit.MINUTES.between(record.getCheckOut().toLocalTime(), currentTime);
                if (minutes < 3) {
                    throw new RuntimeException("请勿在3分钟内重复打卡下班");
                }
            }

            record.setCheckOut(java.sql.Time.valueOf(currentTime));
            record.setStatus(currentTime.isBefore(endTime) ? "早退" : "正常");
            attendanceRecordsMapper.updateById(record);

            // 处理凌晨下班补昨天旷工
            if (currentTime.isAfter(LocalTime.MIDNIGHT) && currentTime.isBefore(LocalTime.of(6, 0))) {
                LocalDate yesterday = today.minusDays(1);
                AttendanceRecords yesterRecord = attendanceRecordsMapper.selectOne(
                        new QueryWrapper<AttendanceRecords>()
                                .eq("employee_id", employeeId)
                                .eq("shift_id", shiftId)
                                .eq("work_date", yesterday)
                );
                if (yesterRecord == null) {
                    AttendanceRecords absentRecord = new AttendanceRecords();
                    absentRecord.setEmployeeId(employeeId);
                    absentRecord.setShiftId(shiftId);
                    absentRecord.setWorkDate(java.sql.Date.valueOf(yesterday));
                    absentRecord.setStatus("旷工");
                    attendanceRecordsMapper.insert(absentRecord);
                }
            }
        }
        // 5. 非法类型
        else {
            throw new RuntimeException("无效的打卡类型");
        }

        // 6. 返回VO
        ClockVo clockVo = new ClockVo();
        clockVo.setStatus(record.getStatus());
        clockVo.setServerTime(now);
        clockVo.setUsername(user.getUsername());
        clockVo.setCheck_in(record.getCheckIn());
        clockVo.setCheck_out(record.getCheckOut());
        return clockVo;
    }

    @Override
    public BQListVo getBQList() {
        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();
        int employeeId = loginUser.getEmployeeId();
        LocalDate today = LocalDate.now();
        // 2. 查询非正常考勤记录
        QueryWrapper<AttendanceRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", employeeId)
                .ne("status", "正常")      // 非正常状态
                .ne("status", "待审批")
                .lt("work_date", today)    // 今天之前
                .orderByDesc("work_date"); // 按日期降序

        List<AttendanceRecords> abnormalRecords = attendanceRecordsMapper.selectList(queryWrapper);
        List<BQrecord> bqRecords = new ArrayList<>();
        for (AttendanceRecords abnormalRecord : abnormalRecords) {
            BQrecord bqrecord = new BQrecord();
            bqrecord.setAttendanceId(abnormalRecord.getId());
            bqrecord.setUsername(loginUser.getUsername());
            bqrecord.setStatus(abnormalRecord.getStatus());
            bqrecord.setWorkdate(abnormalRecord.getWorkDate());
            bqrecord.setCheck_in(abnormalRecord.getCheckIn());
            bqrecord.setCheck_out(abnormalRecord.getCheckOut());
            bqRecords.add(bqrecord);
        }
        BQListVo bqListVo = new BQListVo();
        bqListVo.setBQList(bqRecords);
        return bqListVo;
    }

    @Resource
    AttendanceRetroactiveMapper attendanceRetroactiveMapper;
    @Override
    public int BQApplay(BQApplay applay) {
        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();
        int employeeId = loginUser.getEmployeeId();

        AttendanceRetroactive attendanceRetroactive = new AttendanceRetroactive();
        attendanceRetroactive.setAttendanceId(applay.getAttendanceId());
        attendanceRetroactive.setEmployeeId(employeeId);
        attendanceRetroactive.setStatus("未审批");

        AttendanceRecords attendanceRecords = attendanceRecordsMapper.selectById(attendanceRetroactive.getAttendanceId());
        attendanceRecords.setStatus("待审批");
        attendanceRecordsMapper.updateById(attendanceRecords);

        // 转换前端字符串为 LocalDate / LocalTime
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        if (applay.getWorkdate() != null && !applay.getWorkdate().isEmpty()) {
            attendanceRetroactive.setWorkDate(LocalDate.parse(applay.getWorkdate(), dateFormatter));
        }

        if (applay.getCheckIn() != null && !applay.getCheckIn().isEmpty()) {
            attendanceRetroactive.setRetroIn(LocalTime.parse(applay.getCheckIn(), timeFormatter));
        }

        if (applay.getCheckOut() != null && !applay.getCheckOut().isEmpty()) {
            attendanceRetroactive.setRetroOut(LocalTime.parse(applay.getCheckOut(), timeFormatter));
        }

        attendanceRetroactive.setReason(applay.getReason());
        attendanceRetroactive.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));


        return attendanceRetroactiveMapper.insert(attendanceRetroactive);
    }
    @Resource
    EmployeesMapper employeesMapper;
    @Override
    public BQApprovListVo getAllBQApprovList() {
        List<AttendanceRetroactive> attendanceRetroactiveList = attendanceRetroactiveMapper.selectList(
                new QueryWrapper<AttendanceRetroactive>()
                        .eq("status", "未审批")
        );
        List<BQApprove> bqApproveList = new ArrayList<>();
        for (AttendanceRetroactive attendanceRetroactive : attendanceRetroactiveList) {
            BQApprove bqApprove = new BQApprove();
            bqApprove.setId(attendanceRetroactive.getId());
            bqApprove.setWorkdate(attendanceRetroactive.getWorkDate());
            int employeeId = attendanceRetroactive.getEmployeeId();
            QueryWrapper<Employees> employeesQueryWrapper = new QueryWrapper<>();
            employeesQueryWrapper.eq("id", employeeId);
            Employees employees = employeesMapper.selectOne(employeesQueryWrapper);
            bqApprove.setUsername(employees.getName());
            bqApprove.setCheck_in(attendanceRetroactive.getRetroIn());
            bqApprove.setCheck_out(attendanceRetroactive.getRetroOut());
            bqApprove.setReason(attendanceRetroactive.getReason());
            bqApprove.setStatus(attendanceRetroactive.getStatus());
            bqApproveList.add(bqApprove);
        }
        BQApprovListVo bqApprovListVo = new BQApprovListVo();
        bqApprovListVo.setBQApproveListVo(bqApproveList);
        return bqApprovListVo;
    }
    @Override
    @Transactional
    public int BQAgree(BQProcessDto bqAgreeDto) {
        if (bqAgreeDto.getAttendanceRetroactiveIds() == null || bqAgreeDto.getAttendanceRetroactiveIds().isEmpty()) {
            throw new IllegalArgumentException("attendanceRetroactiveIds 不能为空");
        }

        Integer attendanceRetroactive_id = bqAgreeDto.getAttendanceRetroactiveIds().get(0);
        Users loginUser = new UserUtils().getLoginUser();
        Integer approver_id = loginUser.getEmployeeId();

        AttendanceRetroactive attendanceRetroactive = attendanceRetroactiveMapper.selectById(attendanceRetroactive_id);
        if (attendanceRetroactive == null) {
            throw new IllegalArgumentException("补签记录不存在");
        }

        attendanceRetroactive.setStatus("同意");
        attendanceRetroactive.setApproverId(approver_id);
        int updatedCount = attendanceRetroactiveMapper.updateById(attendanceRetroactive);

        // 更新考勤记录为正常
        Integer attendanceRecords_id = attendanceRetroactive.getAttendanceId();
        if (attendanceRecords_id != null) {
            AttendanceRecords attendanceRecords = attendanceRecordsMapper.selectById(attendanceRecords_id);
            if (attendanceRecords != null) {
                attendanceRecords.setStatus("正常");
                attendanceRecordsMapper.updateById(attendanceRecords);
            }
        }

        return updatedCount;
    }
    @Override
    public int BQReject (BQProcessDto bqAgreeDto) {
        int attendanceRetroactive_id = bqAgreeDto.getAttendanceRetroactiveIds().get(0);
        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();
        int approver_id= loginUser.getEmployeeId();
        AttendanceRetroactive attendanceRetroactive = attendanceRetroactiveMapper.selectById(attendanceRetroactive_id);
        attendanceRetroactive.setStatus("驳回");
        attendanceRetroactive.setApproverId(approver_id);
        return attendanceRetroactiveMapper.updateById(attendanceRetroactive);

    }

    @Override
    public int BQAgreeAll(BQProcessDto bqAgreeDto) {
        List<Integer> AttendanceRetroactiveIds = bqAgreeDto.getAttendanceRetroactiveIds();
        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();
        int approver_id= loginUser.getEmployeeId();
        int count = 0;

        for (Integer retroactiveId : AttendanceRetroactiveIds) {
            AttendanceRetroactive attendanceRetroactive = attendanceRetroactiveMapper.selectById(retroactiveId);
            if (attendanceRetroactive == null) {
                continue; // 没查到就跳过
            }

            // 更新补签申请表
            attendanceRetroactive.setStatus("同意");
            attendanceRetroactive.setApproverId(approver_id);
            attendanceRetroactiveMapper.updateById(attendanceRetroactive);

            // 更新考勤记录表
            int attendanceRecordsId = attendanceRetroactive.getAttendanceId();
            AttendanceRecords attendanceRecords = attendanceRecordsMapper.selectById(attendanceRecordsId);
            if (attendanceRecords != null) {
                attendanceRecords.setStatus("正常");
                attendanceRecordsMapper.updateById(attendanceRecords);
            }

            count++;
        }

        return count; // 返回处理的记录数

    }

    @Override
    public int BQRejectAll(BQProcessDto bqRejectDto) {
        List<Integer> attendanceRetroactiveIds = bqRejectDto.getAttendanceRetroactiveIds();
        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();
        int approver_id= loginUser.getEmployeeId();
        int count = 0;

        for (Integer retroactiveId : attendanceRetroactiveIds) {
            AttendanceRetroactive attendanceRetroactive = attendanceRetroactiveMapper.selectById(retroactiveId);
            if (attendanceRetroactive == null) {
                continue; // 没查到就跳过
            }

            // 更新补签申请表
            attendanceRetroactive.setStatus("驳回");
            attendanceRetroactive.setApproverId(approver_id);
            attendanceRetroactiveMapper.updateById(attendanceRetroactive);

            count++;
        }

        return count; // 返回处理的记录数
    }

    @Override
    public AttendanceRecordsVo getAttendanceRecords(int month) {
        UserUtils userUtils = new UserUtils();
        Users loginUser = userUtils.getLoginUser();
        int employeeId = loginUser.getEmployeeId();
        int year = LocalDate.now().getYear();
        LocalDate today = LocalDate.now();
        AttendanceRecordsVo attendanceRecordsVo = new AttendanceRecordsVo();
        attendanceRecordsVo.setAttendanceRecord(attendanceRecordsMapper.getAttendanceCalendar(employeeId, year, month, today));
        attendanceRecordsVo.setMonth(month);
        YearMonth yearMonth = YearMonth.of(year, month);
        int totalDays = yearMonth.lengthOfMonth();
        attendanceRecordsVo.setDays(totalDays);
        return attendanceRecordsVo;
    }

    public CheckInCountVo CheckInCount(){
        // 获取今天日期
        LocalDate today = LocalDate.now();

        // 构造查询条件
        QueryWrapper<AttendanceRecords> wrapper = new QueryWrapper<>();
        wrapper.eq("work_date", today)      // 当天
                .isNotNull("check_in");      // 上班打卡不为空

        // 查询符合条件的记录数
        int count = attendanceRecordsMapper.selectCount(wrapper);
        CheckInCountVo checkInCountVo = new CheckInCountVo();
        checkInCountVo.setAttendanceCount(count);
        List<AttendEmployeeInfo> list = employeesMapper.findAllAttendEmployeeInfo();
        checkInCountVo.setAttendEmployeeInfoList(list);
        return checkInCountVo;
    }
}
