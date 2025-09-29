package com.neusoft.service;

import com.neusoft.dto.ApproverChganeDto;
import com.neusoft.dto.ApproverConfigDto;
import com.neusoft.vo.ApproversConfigVo;

public interface IApproverConfigService {
    ApproversConfigVo getAllApprover(ApproverConfigDto approverConfigDto);
    boolean ApproverChange(ApproverChganeDto approverChganeDto);
}
