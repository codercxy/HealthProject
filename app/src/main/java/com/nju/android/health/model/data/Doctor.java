package com.nju.android.health.model.data;

/**
 * Created by 57248 on 2016/9/29.
 */

public class Doctor {
    private String name;
    private String office;
    private String loc;
    private int num;
    private double good;
    private Integer picRes;

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public double getGood() {
        return good;
    }

    public void setGood(double good) {
        this.good = good;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Integer getPicRes() {
        return picRes;
    }

    public void setPicRes(Integer picRes) {
        this.picRes = picRes;
    }


}
