package com.neusoft.service.impl;

import com.neusoft.dto.ApproverChganeDto;
import com.neusoft.dto.ApproverConfigDto;
import com.neusoft.mapper.ApproverMapper;
import com.neusoft.service.IApproverConfigService;
import com.neusoft.vo.Approvers;
import com.neusoft.vo.ApproversConfigVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApproverConfigServiceImpl implements IApproverConfigService {
    @Resource
    ApproverMapper approverMapper;
    @Override
    public ApproversConfigVo getAllApprover(ApproverConfigDto approverConfigDto){
        List<Approvers> list = approverMapper.getAllApprovers(approverConfigDto);
        ApproversConfigVo approversConfigVo = new ApproversConfigVo();
        approversConfigVo.setApprovers(list);
        return approversConfigVo;
    }

    @Override
    public boolean ApproverChange(ApproverChganeDto dto){
        boolean attendanceSuccess = true;
        boolean vacationSuccess = true;
        // 更新考勤审批员
        if(dto.getAttendance() != null ){
            approverMapper.deleteOldApprover(dto.getDept_id(), "考勤审批");
            int inserted = approverMapper.insertNewApprover(dto.getAttendance().getEmployeeid(), "考勤审批");
            attendanceSuccess = inserted > 0;
        }

        // 更新假期审批员
        if(dto.getVacation() != null ){
            approverMapper.deleteOldApprover(dto.getDept_id(), "假期审批");
            int inserted = approverMapper.insertNewApprover(dto.getVacation().getEmployeeid(), "假期审批");
            vacationSuccess = inserted > 0;
        }
        return attendanceSuccess && vacationSuccess;
    }

}
