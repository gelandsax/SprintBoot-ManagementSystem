package com.neusoft.controller;

import com.neusoft.dto.WorkShiftDto;
import com.neusoft.response.ApiResponse;
import com.neusoft.service.IWorkShiftService;
import com.neusoft.vo.WorkShiftVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WorkShiftController {

    @Autowired
    IWorkShiftService iWorkShiftService;

    @GetMapping("/workShift/shift/{id}")
    public ApiResponse<WorkShiftVo> geyShift(@PathVariable Integer id){
        WorkShiftVo workShiftVo = iWorkShiftService.getShift(id);
        return ApiResponse.success(workShiftVo);
    }
    @GetMapping("/workShift/allShift")
    public ApiResponse<List<WorkShiftVo>> getAllShift(){
        List<WorkShiftVo> workShiftVoList = iWorkShiftService.getAllShift();
        return ApiResponse.success(workShiftVoList);
    }
    @PostMapping("/workShift/addShift")
    public ApiResponse<Boolean> addShift(@RequestBody WorkShiftDto workShiftDto){
        boolean result = iWorkShiftService.addShift(workShiftDto);
        return ApiResponse.success(result);
    }
    @PutMapping("/workShift/updateShift/{id}")
    public ApiResponse<Boolean> updateShift(@RequestBody WorkShiftDto workShiftDto,
                                            @PathVariable Integer id){
        boolean result = iWorkShiftService.updateShift(workShiftDto,id);
        return ApiResponse.success(result);
    }
    @DeleteMapping("workShift/deleteShift/{id}")
    public ApiResponse<Boolean> deleteShift(@PathVariable Integer id){
        boolean result = iWorkShiftService.deleteShift(id);
        return ApiResponse.success(result);
    }
}
