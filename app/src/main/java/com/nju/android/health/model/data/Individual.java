package com.nju.android.health.model.data;

/**
 * Created by 57248 on 2016/8/26.
 */
public class Individual {
    private String name;
    private String sex;
    private int age;
    private String birth;
    private int height;
    private int weight;
    private String occupation;
    private String location;
    private String address;
    private String familyHistory;
    private String marriage;
    private String child;
    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    private long user_id;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getBirth() {
        return birth;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getFamilyHistory() {
        return familyHistory;
    }
    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }
    public String getMarriage() {
        return marriage;
    }
    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }
    public String getChild() {
        return child;
    }
    public void setChild(String child) {
        this.child = child;
    }
}
