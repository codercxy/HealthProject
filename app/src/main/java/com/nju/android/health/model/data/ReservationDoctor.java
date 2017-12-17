package com.nju.android.health.model.data;

import java.util.List;

/**
 * Created by chy on 2017/12/6.
 */

public class ReservationDoctor {
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

    public String getGoodAt() {
        return goodAt;
    }

    public void setGoodAt(String goodAt) {
        this.goodAt = goodAt;
    }


    private String name;
    private String level;
    private String goodAt;

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    private boolean isVisible;

    public List<String> getRemain() {
        return remain;
    }

    public void setRemain(List<String> remain) {
        this.remain = remain;
    }

    private List<String> remain;

}
