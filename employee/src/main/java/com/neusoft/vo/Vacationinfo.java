package com.neusoft.vo;

public class Vacationinfo {
    private int EVBid;
    private String typename;
    private boolean limit;
    private int remaindays;

    public void setEVBid(int EVBid) {
        this.EVBid = EVBid;
    }
    public int getEVBid() {
        return EVBid;
    }
    public void setTypename(String typename) {
        this.typename = typename;
    }
    public String getTypename() {
        return typename;
    }
    public void setLimit(boolean limit) {
        this.limit = limit;
    }
    public boolean getLimit() {
        return limit;
    }
    public void setRemaindays(int remaindays) {
        this.remaindays = remaindays;
    }
    public int getRemaindays() {
        return remaindays;
    }

}
