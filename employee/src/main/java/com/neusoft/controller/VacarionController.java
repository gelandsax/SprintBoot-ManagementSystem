package com.neusoft.controller;

import com.neusoft.dto.VacationProcessDto;
import com.neusoft.dto.VacationReq;
import com.neusoft.dto.VacationReqDto;
import com.neusoft.response.ApiResponse;
import com.neusoft.service.IvocationService;
import com.neusoft.vo.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class VacarionController {
    @Resource
    IvocationService vacationService;

    @GetMapping("/vacation")
    public ApiResponse<VacationCountVo> GetVocarionCount() {
        return ApiResponse.success(vacationService.getVocationCount());
    }

    @GetMapping("/vacation/getAllVacationType")
    public ApiResponse<VacationTypeListVo> getAllVacationType() {
        return ApiResponse.success(vacationService.getVacationType());
    }

    @PostMapping("/vacation/request")
    public ApiResponse VocarionRequest(@RequestBody VacationReqDto vocationReqDto) {
        int count = vacationService.vacationRequest(vocationReqDto.getVacationReq());
        if(count == 1){
            return ApiResponse.success();
        }else{
            return ApiResponse.error(500,"请求失败");
        }
    }

    @GetMapping("/vacation/myRequestList")
    public ApiResponse<MyVacationRequestListVo> GetMyVacationRequestList() {
        return ApiResponse.success(vacationService.getMyVacationRequestList());
    }

    @GetMapping("/vacation/AllRequestList")
    public ApiResponse<MyVacationRequestListVo> getAllVacationRequestList() {
        return ApiResponse.success(vacationService.getAllVacationRequestList());
    }

    @GetMapping("/vacationApprove")
    public ApiResponse<VacationApproveListVo> GetVacationApproveList() {
        return ApiResponse.success(vacationService.getVacationApproveList());
    }

    @PostMapping("/vacationApprove/agree")
    public ApiResponse AgreeVacation (@RequestBody VacationProcessDto vacationProcessDto) {
        int count = vacationService.agreeVacation(vacationProcessDto);
        if(count == 1){
            return ApiResponse.success();
        }else{
            return ApiResponse.error(500,"更新失败");
        }
    }

    @PostMapping("/vacationApprove/reject")
    public ApiResponse RejectVacation (@RequestBody VacationProcessDto vacationProcessDto) {
        int count = vacationService.rejectVacation(vacationProcessDto);
        if(count == 1){
            return ApiResponse.success();
        }else{
            return ApiResponse.error(500,"更新失败");
        }
    }

    @PostMapping("/vacationApprove/agreeAll")
    public ApiResponse AgreeAllVacation (@RequestBody VacationProcessDto vacationProcessDto) {
        int count = vacationService.agreeAllVacations(vacationProcessDto);
        if(count == vacationProcessDto.getVacationRequestIds().size()){
            return ApiResponse.success();
        }else{
            return ApiResponse.error(500,"更新失败");
        }
    }

    @PostMapping("/vacationApprove/rejectAll")
    public ApiResponse RejectAllVacation (@RequestBody VacationProcessDto vacationProcessDto) {
        int count = vacationService.rejectAllVacations(vacationProcessDto);
        if(count == vacationProcessDto.getVacationRequestIds().size()){
            return ApiResponse.success();
        }else{
            return ApiResponse.error(500,"更新失败");
        }
    }

    @PostMapping("/vacation/config")
    public ApiResponse VacationConfig(@RequestBody VacationConfigVo vacationConfigVo) {
        try {
            int result = vacationService.VacationConfig(vacationConfigVo);
            return ApiResponse.success(result);
        } catch (RuntimeException e) {
            return ApiResponse.error(500,e.getMessage());
        }
    }

    @PostMapping("/vacation/delete")
    public ApiResponse VacationDelete(@RequestBody VacationConfigVo vacationConfigVo) {
        try {
            int result = vacationService.deleteVacation(vacationConfigVo);
            return ApiResponse.success(result);
        } catch (RuntimeException e) {
            return ApiResponse.error(500,e.getMessage());
        }
    }

}
