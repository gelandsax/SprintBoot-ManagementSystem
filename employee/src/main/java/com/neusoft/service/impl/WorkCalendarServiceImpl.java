package com.neusoft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.dto.WorkCalendarConfigDto;
import com.neusoft.mapper.WorkCalendarMapper;
import com.neusoft.model.WorkCalendar;
import com.neusoft.service.IWorkCalendarService;
import com.neusoft.utils.CalendarInitUtils;
import com.neusoft.vo.InitWorkCalendarVo;
import com.neusoft.vo.WorkCalendarDayType;
import com.neusoft.vo.WorkCalendarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkCalendarServiceImpl implements IWorkCalendarService {
    @Resource
    WorkCalendarMapper workCalendarMapper;
    @Resource
    CalendarInitUtils calendarInitUtils;
    @Override
    public InitWorkCalendarVo InitWorkCalendar(int year) {
        calendarInitUtils.initYearCalendar(year);
        QueryWrapper<WorkCalendar> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("EXTRACT(YEAR FROM work_date) = {0}", year);
        int days = workCalendarMapper.selectCount(queryWrapper);
        InitWorkCalendarVo vo = new InitWorkCalendarVo();
        vo.setDays(days);
        vo.setYear(year);
        return vo;
    }

    @Override
    public WorkCalendarVo getWorkCalendar(int year){
        WorkCalendarVo workCalendarVo = new WorkCalendarVo();
        List<WorkCalendarDayType> workCalendarDayTypes = new ArrayList<>();
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        List<WorkCalendar> records = workCalendarMapper.selectList(
                new QueryWrapper<WorkCalendar>()
                        .ge("work_date", java.sql.Date.valueOf(start))
                        .le("work_date", java.sql.Date.valueOf(end))
                        .orderByAsc("work_date")
        );
        for(WorkCalendar record : records){
            WorkCalendarDayType workCalendarDayType = new WorkCalendarDayType();
            workCalendarDayType.setDate(record.getWorkDate());
            workCalendarDayType.setDayType(record.getDayType());
            workCalendarDayTypes.add(workCalendarDayType);
        }
        workCalendarVo.setYear(year);
        workCalendarVo.setDays(workCalendarDayTypes.size());
        workCalendarVo.setWorkCalendar(workCalendarDayTypes);
        return workCalendarVo;
    }

    @Override
    public WorkCalendarVo getWorkCalendarByMonth(int month) {
        int year = LocalDate.now().getYear();

        WorkCalendarVo workCalendarVo = new WorkCalendarVo();
        List<WorkCalendarDayType> workCalendarDayTypes = new ArrayList<>();

        // 获取指定月份的开始和结束日期
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        // 查询数据库中该月份的日历记录，并按日期升序排序
        List<WorkCalendar> records = workCalendarMapper.selectList(
                new QueryWrapper<WorkCalendar>()
                        .ge("work_date", java.sql.Date.valueOf(start))
                        .le("work_date", java.sql.Date.valueOf(end))
                        .orderByAsc("work_date")
        );

        // 遍历记录并封装
        for (WorkCalendar record : records) {
            WorkCalendarDayType dayType = new WorkCalendarDayType();
            dayType.setDate(record.getWorkDate());
            dayType.setDayType(record.getDayType());
            workCalendarDayTypes.add(dayType);
        }

        // 设置返回对象
        workCalendarVo.setYear(year);
        workCalendarVo.setDays(workCalendarDayTypes.size());
        workCalendarVo.setWorkCalendar(workCalendarDayTypes);

        return workCalendarVo;
    }
    @Override
    public WorkCalendarVo WorkCalendarConfig(WorkCalendarConfigDto workCalendarConfigDto){
        String dateStr = workCalendarConfigDto.getDate();    // e.g., "2025-09-26"
        String dayType = workCalendarConfigDto.getDay_type(); // e.g., "调休日"

        // 1️⃣ 将字符串转换为 LocalDate
        LocalDate date = LocalDate.parse(dateStr);
        int year = date.getYear();
        // 2️⃣ 查询数据库中该日期的记录
        QueryWrapper<WorkCalendar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_date", java.sql.Date.valueOf(date));

        WorkCalendar calendar = workCalendarMapper.selectOne(queryWrapper);

        if (calendar != null) {
            // 3️⃣ 修改 dayType 并更新数据库
            calendar.setDayType(dayType);
            workCalendarMapper.updateById(calendar);
        } else {
            // 如果记录不存在，可以选择插入一条新记录
            calendar = new WorkCalendar();
            calendar.setWorkDate(java.sql.Date.valueOf(date));
            calendar.setDayType(dayType);
            workCalendarMapper.insert(calendar);
        }

        return getWorkCalendar(year);
    }
}
