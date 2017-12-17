package com.nju.android.health.model.data;

/**
 * Created by chy on 2017/12/15.
 */

public class ReservationDetail {
    private String starttime;

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    private int remain;
}
