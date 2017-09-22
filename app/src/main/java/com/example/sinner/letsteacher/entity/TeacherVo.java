package com.example.sinner.letsteacher.entity;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by win7 on 2016-12-26.
 */

public class TeacherVo extends BmobObject {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String type;
    private String salary;
    private ArrayList<TeachTimeVo> times;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public ArrayList<TeachTimeVo> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<TeachTimeVo> times) {
        this.times = times;
    }
}
