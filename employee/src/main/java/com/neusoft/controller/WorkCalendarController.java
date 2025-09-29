package com.neusoft.controller;

import com.neusoft.dto.WorkCalendarConfigDto;
import com.neusoft.response.ApiResponse;
import com.neusoft.service.IWorkCalendarService;
import com.neusoft.vo.InitWorkCalendarVo;
import com.neusoft.vo.WorkCalendarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class WorkCalendarController {
    @Resource
    IWorkCalendarService workCalendarService;
    @PostMapping("/InitWorkCalendar/{year}")
    public ApiResponse<InitWorkCalendarVo> initWorkCalendar(@PathVariable int year) {
        InitWorkCalendarVo vo;
        vo = workCalendarService.InitWorkCalendar(year);
        return ApiResponse.success(vo);
    }

    @GetMapping("/GetWorkCalendar/{year}")
    public ApiResponse<WorkCalendarVo> getWorkCalendar(@PathVariable int year) {
        WorkCalendarVo vo;
        vo = workCalendarService.getWorkCalendar(year);
        return ApiResponse.success(vo);
    }

    @PostMapping("/ConfigWorkCalendar")
    public ApiResponse<WorkCalendarVo> configWorkCalendar(WorkCalendarConfigDto workCalendarConfigDto) {
        WorkCalendarVo vo = workCalendarService.WorkCalendarConfig(workCalendarConfigDto);
        return ApiResponse.success(vo);
    }
}
