package com.neusoft.vo;

public class VacationTypeVo {
    private int id;
    private String vacationType;
    private String limitType;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setVacationType(String vacationType) {
        this.vacationType = vacationType;
    }
    public String getVacationType() {
        return vacationType;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }
}
