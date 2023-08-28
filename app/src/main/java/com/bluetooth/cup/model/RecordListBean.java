package com.bluetooth.cup.model;

import com.orm.SugarRecord;

public class RecordListBean extends SugarRecord {
    private String openTime;
    private String closeTime;
    private String weeks;
    private String electricity;

    public RecordListBean(){}

    public RecordListBean(String openTime, String closeTime, String weeks,String electricity) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.weeks = weeks;
        this.electricity = electricity;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }
}
