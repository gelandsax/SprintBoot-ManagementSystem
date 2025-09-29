package com.neusoft.controller;

import com.neusoft.dto.ApproverChganeDto;
import com.neusoft.dto.ApproverConfigDto;
import com.neusoft.response.ApiResponse;
import com.neusoft.service.IApproverConfigService;
import com.neusoft.service.IEmployeesService;
import com.neusoft.vo.ApproversConfigVo;
import com.neusoft.vo.EmplyeesListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApproverController {
    @Autowired
    IApproverConfigService approverConfigService;
    @Autowired
    IEmployeesService employeesService;
    @GetMapping("/Approver/getApprover")
    public ApiResponse<ApproversConfigVo> getApproverList(ApproverConfigDto approverConfigDto) {
        return ApiResponse.success(approverConfigService.getAllApprover(approverConfigDto));
    }

    @PostMapping("/employee/getByDept/{dept_id}")
    public ApiResponse<EmplyeesListVo> getEmployeeByDept(@PathVariable int dept_id) {
        EmplyeesListVo vo = employeesService.getEmployeesByDepartment(dept_id);
        return ApiResponse.success(vo);
    }

    @PostMapping("/Approver/config")
    public ApiResponse ApproverChange(ApproverChganeDto approverChganeDto) {
        if(approverConfigService.ApproverChange(approverChganeDto)){
            return ApiResponse.success();
        }else{
            return ApiResponse.error(500,"更新失败");
        }
    }
}
