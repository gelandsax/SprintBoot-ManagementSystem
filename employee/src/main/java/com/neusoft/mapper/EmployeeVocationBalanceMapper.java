package com.neusoft.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.dto.Employee;
import com.neusoft.model.EmployeeVacationBalance;
import org.apache.ibatis.annotations.Param;

public interface EmployeeVocationBalanceMapper extends BaseMapper<EmployeeVacationBalance> {
    int updateVacation(@Param("employeeId") Integer employeeId,
                       @Param("vacationTypeId") Integer vacationTypeId,
                       @Param("year") Integer year,
                       @Param("totalDays") Integer totalDays,
                       @Param("usedDays") Integer usedDays);
}
