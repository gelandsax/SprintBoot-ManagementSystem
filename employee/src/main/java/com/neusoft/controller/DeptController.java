package com.neusoft.controller;

import com.neusoft.dto.AddDeptDto;
import com.neusoft.dto.UpdateDeptmentsDto;
import com.neusoft.response.ApiResponse;
import com.neusoft.service.IDeptService;
import com.neusoft.vo.DeptVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DeptController {
    @Autowired
    IDeptService iDeptService;
    @GetMapping("/dept/info/{id}")
    public ApiResponse<DeptVo> getDeptment(@PathVariable Integer id){
            DeptVo deptVo = iDeptService.getDeptment(id);
            return ApiResponse.success(deptVo);
    }

    @GetMapping("/all/deptment")
    public ApiResponse<List<DeptVo>> getAllDeptment()
    {
        List<DeptVo> deptVoList = iDeptService.getAllDeptment();
        return ApiResponse.success(deptVoList);
    }
    @PostMapping("/dept/addDept")
    public ApiResponse<Boolean> addDeptment(@RequestBody AddDeptDto addDeptDto){
        boolean result = iDeptService.addDeptment(addDeptDto);
        return ApiResponse.success(result);
    }
    @PutMapping("/dept/updateDept/{id}")
    public ApiResponse<Boolean> updateDeptment(@RequestBody UpdateDeptmentsDto updateDeptmentsDto,
                                               @PathVariable Integer id){
       boolean result = iDeptService.updateDeptments(updateDeptmentsDto, id);
       return ApiResponse.success(result);
    }
    @PutMapping("/dept/updateStatus/{id}")
    public ApiResponse<Boolean> updateDeptStatus(@PathVariable Integer id){
        boolean result = iDeptService.updateDeptStatus(id);
        return ApiResponse.success(result);
    }
}
