package com.example.sinner.letsteacher.application;

import android.app.Application;
import android.content.Intent;

import com.example.sinner.letsteacher.entity.UserVo;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by sinner on 2017-11-08.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
@DefaultLifeCycle(application = ".application.DefaultApplication", flags = ShareConstants.TINKER_ENABLE_ALL)
public class SampleApplicationLike extends DefaultApplicationLike {

    private boolean isDebug=true;
    private UserVo userVo;


    public SampleApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, ShareConstants.TINKER_ENABLE_ALL, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TinkerInstaller.install(this);
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
