package com.nju.android.health.model.data;

/**
 * Created by 57248 on 2016/10/19.
 */

public class Step {

    private int number;
    private String date;
    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    private long user_id;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
