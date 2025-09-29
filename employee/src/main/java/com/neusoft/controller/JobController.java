package com.neusoft.controller;

import com.neusoft.dto.AddJobDto;
import com.neusoft.dto.UpdateJobDto;
import com.neusoft.response.ApiResponse;
import com.neusoft.service.IJobService;
import com.neusoft.vo.JobVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class JobController {
    @Autowired
    IJobService iJobService;
    @GetMapping("/job/info/{id}")
    public ApiResponse<JobVo> getJob(@PathVariable Integer id){
        JobVo jobVo = iJobService.getJob(id);
        return ApiResponse.success(jobVo);
    }
    @GetMapping("/job/allJob")
    public ApiResponse<List<JobVo>> getAllJob(){
       List<JobVo> jobVoList = iJobService.getAllJob();
       return ApiResponse.success(jobVoList);
    }
    @PostMapping("/job/addjob")
    public ApiResponse<Boolean> addJob(@RequestBody  AddJobDto addJobDto){
        boolean result = iJobService.addJob(addJobDto);
        return ApiResponse.success(result);
    }
    @PutMapping("/job/updateJob/{id}")
    public ApiResponse<Boolean> updateJob(@RequestBody UpdateJobDto updateJobDto, @PathVariable Integer id){
        boolean result = iJobService.updateJob(updateJobDto,id);
        return ApiResponse.success(result);
    }
    @PutMapping("/job/updateStatus/{id}")
    public ApiResponse<Boolean> updateStatus(@PathVariable Integer id){
        boolean result = iJobService.updateStatus(id);
        return ApiResponse.success(result);
    }
}
