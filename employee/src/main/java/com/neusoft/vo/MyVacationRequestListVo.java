package com.neusoft.vo;

import com.neusoft.dto.VacationReq;

import java.util.List;

public class MyVacationRequestListVo {
    private List<VacationReqVo> vocationReqList;

    public void setVocationReqList(List<VacationReqVo> vocationReqList) {
        this.vocationReqList = vocationReqList;
    }
    public List<VacationReqVo> getVocationReqList() {
        return vocationReqList;
    }
}
