package com.nju.android.health.model.data;

/**
 * Created by chy on 2017/12/5.
 */

public class Hospital {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    private String level;
    private Integer resId;

}
