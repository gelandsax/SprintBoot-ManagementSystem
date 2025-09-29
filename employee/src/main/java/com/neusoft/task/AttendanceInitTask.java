package com.neusoft.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.mapper.AttendanceRecordsMapper;
import com.neusoft.mapper.EmployeeShiftsMapper;
import com.neusoft.mapper.UsersMapper;
import com.neusoft.mapper.VacationRequestsMapper;
import com.neusoft.model.AttendanceRecords;
import com.neusoft.model.EmployeeShifts;
import com.neusoft.model.Users;
import com.neusoft.model.VacationRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.neusoft.utils.HolidayUtils;
import java.time.LocalDate;
import java.util.List;

@Component
public class AttendanceInitTask {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private EmployeeShiftsMapper employeeShiftsMapper;

    @Autowired
    private AttendanceRecordsMapper attendanceRecordsMapper;

    @Autowired
    private VacationRequestsMapper vacationRequestsMapper;

    @Autowired
    private HolidayUtils holidayUtils;

    @Scheduled(cron = "0 0 0 * * ?") // 每天0点执行
    public void initDailyAttendance() {
        LocalDate today = LocalDate.now();

        boolean isHoliday = holidayUtils.isHoliday(today);

        // 获取所有员工
        List<Users> allUsers = usersMapper.selectList(null);

        for (Users user : allUsers) {
            int employeeId = user.getEmployeeId();

            // 获取员工最新班次
            QueryWrapper<EmployeeShifts> shiftWrapper = new QueryWrapper<>();
            shiftWrapper.eq("employee_id", employeeId);
            EmployeeShifts employeeShift = employeeShiftsMapper.selectOne(shiftWrapper);
            if (employeeShift == null) continue;

            int shiftId = employeeShift.getShiftId();

            // 检查当天是否已有打卡记录
            QueryWrapper<AttendanceRecords> recordWrapper = new QueryWrapper<>();
            recordWrapper.eq("employee_id", employeeId)
                    .eq("shift_id", shiftId)
                    .eq("work_date", today);
            AttendanceRecords existing = attendanceRecordsMapper.selectOne(recordWrapper);
            if (existing != null) continue;

            // 创建打卡记录
            AttendanceRecords record = new AttendanceRecords();
            record.setEmployeeId(employeeId);
            record.setShiftId(shiftId);
            record.setWorkDate(java.sql.Date.valueOf(today));

            if (isHoliday) {
                record.setStatus("节假日休息");
            } else {
                // 查询员工是否在请假中（已批准的假期）
                QueryWrapper<VacationRequests> vacationWrapper = new QueryWrapper<>();
                vacationWrapper.eq("employee_id", employeeId)
                        .le("start_date", today)  // start_date <= today
                        .ge("end_date", today)    // end_date >= today
                        .eq("status", "approved")
                        .orderByDesc("created_at")
                        .last("LIMIT 1");
                VacationRequests leave = vacationRequestsMapper.selectOne(vacationWrapper);

                if (leave != null) {
                    record.setStatus("休假中");
                } else {
                    record.setStatus("未打卡");
                }
            }

            attendanceRecordsMapper.insert(record);
        }
    }
}



