package com.neusoft.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.model.WorkCalendar;
import org.apache.ibatis.annotations.Param;

public interface WorkCalendarMapper extends BaseMapper<WorkCalendar> {
    WorkCalendar findByCalendarDate(@Param("calendarDate") java.time.LocalDate calendarDate);
}
