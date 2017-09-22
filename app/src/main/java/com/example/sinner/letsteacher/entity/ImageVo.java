package com.example.sinner.letsteacher.entity;

import com.pigcms.library.android.entify.BasicVo;

import cn.bmob.v3.BmobObject;

/**
 * Created by sinner on 2017-08-10.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class ImageVo extends BmobObject {
    private String file_url;
    private String userid;
    private String groupid;

    public String getGroup() {
        return groupid;
    }

    public void setGroup(String group) {
        this.groupid = group;
    }


    public ImageVo(){

    }
    public  ImageVo(String userid, String  file_url){
        this.file_url=file_url;
        this.userid=userid;
    }

    public  ImageVo(String userid, String  file_url,String groupname){
        this.file_url=file_url;
        this.userid=userid;
        this.groupid=groupname;
    }


    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String username) {
        this.userid = username;
    }
}
