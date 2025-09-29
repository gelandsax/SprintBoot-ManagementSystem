package com.neusoft.service;

import com.neusoft.dto.WorkCalendarConfigDto;
import com.neusoft.vo.InitWorkCalendarVo;
import com.neusoft.vo.WorkCalendarVo;

public interface IWorkCalendarService {
    InitWorkCalendarVo InitWorkCalendar(int year);
    WorkCalendarVo getWorkCalendar(int year);
    WorkCalendarVo getWorkCalendarByMonth(int month);
    WorkCalendarVo WorkCalendarConfig(WorkCalendarConfigDto workCalendarConfigDto);
}
