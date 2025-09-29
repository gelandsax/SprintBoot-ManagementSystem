package com.neusoft.service;

import com.neusoft.dto.VacationProcessDto;
import com.neusoft.dto.VacationReq;
import com.neusoft.vo.*;

import java.time.LocalDate;

public interface IvocationService{
    void refreshAnnualAndFixedVacations();
    int calculateAnnualLeave(LocalDate firstWorkDate);
    VacationTypeListVo getVacationType();
    VacationCountVo getVocationCount();
    int vacationRequest(VacationReq vacationReq);
    MyVacationRequestListVo getMyVacationRequestList();
    MyVacationRequestListVo getAllVacationRequestList();
    VacationApproveListVo getVacationApproveList();
    int agreeVacation(VacationProcessDto vacationProcessDto);
    int rejectVacation(VacationProcessDto vacationProcessDto);
    int agreeAllVacations(VacationProcessDto vacationProcessDto);
    int rejectAllVacations(VacationProcessDto vacationProcessDto);
    int VacationConfig(VacationConfigVo vacationConfigVo);
    int deleteVacation(VacationConfigVo vacationConfigVo);
}
