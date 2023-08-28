package com.bluetooth.cup.model;

/**
 * Created by 阿飞の小蝴蝶 on 2023/7/31
 * Describe:
 */
public class RecorderBean {
    private String week;
    private String openTime;
    private String closeTime;

    public RecorderBean(){}

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
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
}
