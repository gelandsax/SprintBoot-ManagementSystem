package com.neusoft.dto;

import java.util.List;

public class VacationProcessDto {
    private List<Integer> VacationRequestIds; // 多个申请记录id


    public List<Integer> getVacationRequestIds() {
        return VacationRequestIds;
    }

    public void setVacationRequestIds(List<Integer> vacationRequestIds) {
        this.VacationRequestIds = vacationRequestIds;
    }
}
