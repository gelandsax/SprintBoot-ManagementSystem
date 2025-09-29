package com.neusoft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.dto.AddJobDto;
import com.neusoft.dto.UpdateJobDto;
import com.neusoft.mapper.DepartmentsMapper;
import com.neusoft.mapper.EmployeesMapper;
import com.neusoft.mapper.JobsMapper;
import com.neusoft.model.Departments;
import com.neusoft.model.Employees;
import com.neusoft.model.Jobs;
import com.neusoft.service.IJobService;
import com.neusoft.vo.JobEmployeeVo;
import com.neusoft.vo.JobVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JobServiceImpl implements IJobService {

    @Resource
    JobsMapper jobsMapper;
    @Resource
    EmployeesMapper employeesMapper;
    @Resource
    DepartmentsMapper departmentsMapper;
    @Override
    public JobVo getJob(Integer id) {
        QueryWrapper<Jobs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Jobs job = jobsMapper.selectOne(queryWrapper);
        JobVo  jobVo = new JobVo();
        //基础信息
        jobVo.setJobId(id);
        jobVo.setJobName(job.getJobName());
        jobVo.setJobLevel(job.getJobLevel());
        jobVo.setStatus(job.getStatus());

        //该职位的员工
        QueryWrapper<Employees> queryEmployeesWrapper = new QueryWrapper<>();
        queryEmployeesWrapper.eq("job_id",job.getId());
        List<Employees> employeesList = employeesMapper.selectList(queryEmployeesWrapper);
        List<JobEmployeeVo> jobEmployeeVoList = new ArrayList<>();
        for (Employees employee: employeesList){
            JobEmployeeVo jobEmployeeVo = new JobEmployeeVo();
            jobEmployeeVo.setEmployeeId(employee.getId());
            jobEmployeeVo.setEmployeeName(employee.getName());
            jobEmployeeVo.setGender(employee.getGender());

            QueryWrapper<Departments> queryDepartmentsWrapper = new QueryWrapper<>();
            queryDepartmentsWrapper.eq("id", employee.getDeptId());
            Departments departments = departmentsMapper.selectOne(queryDepartmentsWrapper);
            jobEmployeeVo.setDeptName(departments.getDeptName());

            // 计算年龄
            Date birthdayDate = employee.getBirthday();
            int age = 0;
            if (birthdayDate != null) {
                try {
                    LocalDate birthday = birthdayDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    LocalDate today = LocalDate.now();
                    age = Period.between(birthday, today).getYears();
                } catch (Exception e) {
                    age = 0;
                }
            }
            jobEmployeeVo.setAge(age);

            jobEmployeeVoList.add(jobEmployeeVo);
        }
        jobVo.setJobEmployeeVoList(jobEmployeeVoList);
        return jobVo;
    }

    @Override
    public List<JobVo> getAllJob() {
        List<Jobs> jobsList = jobsMapper.selectList(null);
        List<JobVo> jobVoList = new ArrayList<>();
        for (Jobs jobs: jobsList){
            JobVo jobVo = getJob(jobs.getId());
            jobVoList.add(jobVo);
        }

        return jobVoList;
    }

    @Override
    public boolean addJob(AddJobDto addJobDto) {
        Integer jobId = addJobDto.getJobId();
        String jobName = addJobDto.getJobName();
        if (jobId == null || jobName == null)
        {
            throw new RuntimeException("填写信息不能为空");
        }
        QueryWrapper<Jobs> queryWrapperId = new QueryWrapper<>();
        queryWrapperId.eq("id", jobId);
        int count = jobsMapper.selectCount(queryWrapperId);
        if(count > 0){
            throw new RuntimeException("部门编号已存在");
        }
        QueryWrapper<Jobs> queryWrapperJobName = new QueryWrapper<>();
        queryWrapperJobName.eq("job_name", jobName);
        int count2 = jobsMapper.selectCount(queryWrapperJobName);
        if(count2 > 0){
            throw new RuntimeException("部门名已存在");
        }
        Jobs jobs = new Jobs();
        jobs.setId(addJobDto.getJobId());
        jobs.setJobName(addJobDto.getJobName());
        jobs.setJobLevel(addJobDto.getJobLevel());
        jobs.setStatus("活动");
        jobs.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        jobsMapper.insert(jobs);

        return true;
    }

    @Override
    public boolean updateJob(UpdateJobDto updateJobDto, Integer id) {
        QueryWrapper<Jobs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Jobs jobs = jobsMapper.selectOne(queryWrapper);
        //
        String jobName = updateJobDto.getJobName();
        QueryWrapper<Jobs> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("job_name",jobName);
        Integer count = jobsMapper.selectCount(queryWrapper1);
        if(count > 0){
            throw  new RuntimeException("职位已存在");
        }

        jobs.setJobName(updateJobDto.getJobName());
        jobs.setJobLevel(updateJobDto.getJobLevel());
        jobsMapper.updateById(jobs);

        return true;
    }

    @Override
    public boolean updateStatus(Integer id) {
        QueryWrapper<Jobs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Jobs jobs = jobsMapper.selectOne(queryWrapper);

        if(jobs.getStatus().equals("活动")){
            jobs.setStatus("撤销");
        }
        else {
            jobs.setStatus("活动");
        }
        int r= jobsMapper.updateById(jobs);
        if(r <= 0){
            return false;
        }
        return true;
    }
}
