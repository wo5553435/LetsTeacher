package com.example.sinner.letsteacher;

import android.app.Application;

import com.example.sinner.letsteacher.entity.UserVo;

/**
 * Created by sinner on 2017-08-20.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class MyApplication extends Application {
    private boolean isDebug=true;
    private UserVo userVo;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    private void init() {
        if(isDebug){
            userVo=new UserVo();
            userVo.setId("001");
            userVo.setUsername("sinner");
            userVo.setPassword("123456");
        }
    }
}
