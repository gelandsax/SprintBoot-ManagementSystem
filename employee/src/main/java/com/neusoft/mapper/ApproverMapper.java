package com.neusoft.mapper;

import com.neusoft.dto.ApproverConfigDto;
import com.neusoft.vo.Approvers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApproverMapper{
    List<Approvers> getAllApprovers(ApproverConfigDto approverConfigDto);
    // 删除指定部门指定权限的原有审批员
    int deleteOldApprover(@Param("deptId") Integer deptId,
                          @Param("permissionName") String permissionName);

    // 插入新的审批员
    int insertNewApprover(@Param("employeeId") Integer employeeId,
                          @Param("permissionName") String permissionName);
}
