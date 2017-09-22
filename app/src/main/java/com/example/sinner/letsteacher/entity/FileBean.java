package com.example.sinner.letsteacher.entity;

import com.pigcms.library.android.okhttp.BaseVo;

import cn.bmob.v3.BmobObject;

/**
 * Created by sinner on 2017-06-27.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class FileBean extends BmobObject {
    private String groupid;
    private String name;
    private String fileurl;
    private String Colorids;
    private String userid;
    private String remark;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private boolean isDelete=false;

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getColorids() {
        return Colorids;
    }

    public void setColorids(String colorids) {
        Colorids = colorids;
    }
}
