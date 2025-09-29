package com.neusoft.controller;

import com.neusoft.dto.*;
import com.neusoft.mapper.EmployeesMapper;
import com.neusoft.model.Departments;
import com.neusoft.model.Jobs;
import com.neusoft.model.WorkShifts;
import com.neusoft.response.ApiResponse;
import com.neusoft.service.IEmployeesService;
import com.neusoft.service.IProfileService;
import com.neusoft.vo.EmployeeShiftVo;
import com.neusoft.vo.EmployeeShitfListVo;
import com.neusoft.vo.Profile;
import com.neusoft.vo.ProfileVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeesController {
    @Autowired
    IProfileService profileService;
    @Autowired
    private EmployeesMapper employeesMapper;
    @Autowired
    IEmployeesService employeesService;
    @GetMapping("/all/workshift")
    public ApiResponse<List<WorkShifts>> getAllWorkshift(){
        List<WorkShifts> workShiftsList = profileService.getAllWorkShift();
        return ApiResponse.success(workShiftsList);
    }
    @GetMapping("/all/departsment")
    public ApiResponse<List<Departments>> getAllDept(){
        List<Departments> departmentsList = profileService.getAllDept();
        return ApiResponse.success(departmentsList);
    }
    @GetMapping("all/jobs")
    public ApiResponse<List<Jobs>> getAllJob(){
        List<Jobs> jobsList = profileService.getAllJob();
        return ApiResponse.success(jobsList);
    }
    @GetMapping("/profiles/{name}")
    public ApiResponse<ProfileVo> getProfile(@PathVariable String name) {
        Profile profile = profileService.getProfileByUserId(name);
        ProfileVo profileVo = new ProfileVo();
        profileVo.setProfile(profile);
        return ApiResponse.success(profileVo);
    }

    @GetMapping("/profiles")
    public ApiResponse<List<ProfileVo>> getAllProfile(){
        List<ProfileVo> profileVoList = profileService.getAllProfiles();
        return ApiResponse.success(profileVoList);
    }
    @PostMapping("/profiles/addemployee")
    public ApiResponse<Boolean> addEmployee(@RequestBody AddEmployeeDto addEmployeeDto)
    {
        boolean result = profileService.addEmployee(addEmployeeDto);
        return ApiResponse.success(result);
    }
    @PutMapping("/profiles/updateemployee/{id}")
    public ApiResponse<Boolean> updateEmployee(@RequestBody UpdateEmployeeDto updateEmployeeDto, @PathVariable Integer id){
        boolean result = profileService.updateEmployee(updateEmployeeDto, id);
        return ApiResponse.success(result);
    }
    @PutMapping("/profiles/status/{id}")
    public ApiResponse<Boolean> statusUpdate(@PathVariable Integer id){
        boolean result = profileService.statusUpdate(id);
        return ApiResponse.success(result);
    }
    @PutMapping("/profiles/transfer/dept/{id}")
    public ApiResponse<Boolean> transferDept(@RequestBody ModifyDeptDto modifyDeptDto,
                                              @PathVariable Integer id)
    {
        boolean resultDept = profileService.modifyDept(modifyDeptDto,id);
        return ApiResponse.success(resultDept);
    }
    @PutMapping("/profiles/transfer/job/{id}")
    public ApiResponse<Boolean> transferJob(@RequestBody ModifyJobDto modifyJobDto,
                                         @PathVariable Integer id)
    {
        boolean resultJob = profileService.modifyJob(modifyJobDto,id);
        return ApiResponse.success(resultJob);
    }

    @GetMapping("/workShift/getAll")
    public ApiResponse<EmployeeShitfListVo> getAllEmployeeShitf(){
        List<EmployeeShiftVo> list = employeesMapper.findAllEmployeeShift();
        EmployeeShitfListVo employeeShitfListVo = new EmployeeShitfListVo();
        employeeShitfListVo.setEmployees(list);
        return ApiResponse.success(employeeShitfListVo);
    }

    @PostMapping("/workShift/config")
    public ApiResponse EmployeeShiftConfig(@RequestBody EmployeeShiftConfigDto employeeShiftConfigDto){
        if(employeesService.configEmployeeShift(employeeShiftConfigDto) == 1){
            return ApiResponse.success();
        }else{
            return ApiResponse.error(500,"更新失败");
        }
    }
}
