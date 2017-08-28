package com.nju.android.health.model.data;

/**
 * Created by 57248 on 2016/8/26.
 */
public class User {
    private String username;
    private String password;
    private String phone;
    private String address;
    private String recentLoginTime;
    private String registerTime;
    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    private long user_id;
    public void setName(String na){
        username = na;
    }
    public void setPassword(String pw){
        password = pw;
    }
    public String getName(){
        return username;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getRecentLoginTime() {
        return recentLoginTime;
    }
    public void setRecentLoginTime(String recentLoginTime) {
        this.recentLoginTime = recentLoginTime;
    }
    public String getRegisterTime() {
        return registerTime;
    }
    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
    public String getPassword(){
        return password;
    }
}
