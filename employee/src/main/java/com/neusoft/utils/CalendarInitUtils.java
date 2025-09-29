package com.neusoft.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.mapper.WorkCalendarMapper;
import com.neusoft.model.WorkCalendar;
import com.neusoft.service.IWorkCalendarService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CalendarInitUtils {
    @Resource
    WorkCalendarMapper workCalendarMapper;

    @Transactional
    public void initYearCalendar(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        // 1️⃣ 先检查该年份是否已有数据
        QueryWrapper<WorkCalendar> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("work_date", java.sql.Date.valueOf(start))
                .le("work_date", java.sql.Date.valueOf(end));
        int count = workCalendarMapper.selectCount(queryWrapper);

        if (count > 0) {
            // 方案二：删除已存在数据，再重新生成
            workCalendarMapper.delete(queryWrapper);
        }

        // 2️⃣ 生成整年的日历
        List<WorkCalendar> calendarList = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            WorkCalendar calendar = new WorkCalendar();
            calendar.setWorkDate(java.sql.Date.valueOf(date));
            // 周一~周五为工作日，周六周日为休息日
            calendar.setDayType(date.getDayOfWeek().getValue() >= 6 ? "休息日" : "工作日");
            calendarList.add(calendar);
        }

        // 3️⃣ 批量插入数据库
        for (WorkCalendar calendar : calendarList) {
            workCalendarMapper.insert(calendar);
        }
    }
}
