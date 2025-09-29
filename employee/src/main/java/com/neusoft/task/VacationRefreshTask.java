package com.neusoft.task;

import com.neusoft.service.IvocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

public class VacationRefreshTask {
    @Resource
    IvocationService vocationService;

    // 每年 1 月 1 日 00:00 执行
    @Scheduled(cron = "0 0 0 1 1 *")
    public void refreshAnnualVacations() {
        vocationService.refreshAnnualAndFixedVacations();
    }
}
