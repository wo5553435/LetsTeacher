package com.pigcms.library.android.okhttp.callback;

/**
 * Created by win7 on 2017-04-15.
 */

public abstract class  PollingCallback implements ResponseCallback {
    private int currentcount=0;
    abstract void OnSuccess(int count ,String value);

    @Override
    public void OnSuccess(String classinfo){
        OnSuccess(currentcount,classinfo);
    }

}
