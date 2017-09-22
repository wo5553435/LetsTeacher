package com.example.sinner.letsteacher.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by win7 on 2016-12-26.
 */

public class TeachTimeVo extends BmobObject {
    private String id;
    private String time_area;
    private String time_weekly;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
