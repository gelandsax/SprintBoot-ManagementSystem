package com.neusoft.utils;

import com.neusoft.mapper.WorkCalendarMapper;
import com.neusoft.model.WorkCalendar;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class HolidayUtils {
    @Autowired
    WorkCalendarMapper workCalendarMapper;

    /**
     * 判断是否为法定节假日
     * @param date 待判断日期
     * @return true 是节假日，否则 false
     */
    public boolean isHoliday(LocalDate date) {
        WorkCalendar calendar = workCalendarMapper.findByCalendarDate(date);
        if (calendar != null) {
            return "HOLIDAY".equals(calendar.getDayType());
        }
        // 默认规则：周末不是节假日
        DayOfWeek day = date.getDayOfWeek();
        return false;
    }

    /**
     * 判断是否为调休工作日（周末被安排上班）
     * @param date 待判断日期
     * @return true 是调休工作日，否则 false
     */
    public boolean isAdjustedWorkday(LocalDate date) {
        WorkCalendar calendar = workCalendarMapper.findByCalendarDate(date);
        return calendar != null && "ADJUSTED_WORKDAY".equals(calendar.getDayType());
    }

    /**
     * 判断是否为实际工作日（包含调休）
     * @param date 待判断日期
     * @return true 是工作日，否则 false
     */
    public boolean isWorkday(LocalDate date) {
        WorkCalendar calendar = workCalendarMapper.findByCalendarDate(date);
        if (calendar != null) {
            return "WORKDAY".equals(calendar.getDayType()) || "ADJUSTED_WORKDAY".equals(calendar.getDayType());
        }
        // 默认规则：周一到周五为工作日
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    /**
     * 判断是否需要打卡（实际工作日且不是节假日）
     * @param date 待判断日期
     * @return true 需要打卡，否则 false
     */
    public boolean needPunch(LocalDate date) {
        return isWorkday(date) && !isHoliday(date);
    }

}
