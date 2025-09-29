package com.neusoft.service;

import com.neusoft.dto.AddJobDto;
import com.neusoft.dto.UpdateJobDto;
import com.neusoft.vo.JobVo;

import java.util.List;

public interface IJobService {
    JobVo getJob(Integer id);
    List<JobVo> getAllJob();
    boolean addJob(AddJobDto addJobDto);
    boolean updateJob(UpdateJobDto updateJobDto, Integer id);
    boolean updateStatus(Integer id);

}
