package com.nju.android.health.model.data;

/**
 * Created by 57248 on 2016/8/26.
 */
public class Pressure {
    private long id;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    private long user_id;
    private int high;
    private int low;
    private int rate;
    private String time;
    private boolean isSend;
    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public boolean isSend() {
        return isSend;
    }
    public void setSend(boolean isSend) {
        this.isSend = isSend;
    }
    public int getHigh() {
        return high;
    }
    public void setHigh(int high) {
        this.high = high;
    }
    public int getLow() {
        return low;
    }
    public void setLow(int low) {
        this.low = low;
    }
    public int getRate() {
        return rate;
    }
    public void setRate(int rate) {
        this.rate = rate;
    }
}
