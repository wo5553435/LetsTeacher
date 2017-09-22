package com.pigcms.library.android.okhttp.callback;

/**这个接口是给rxjava请求回调用的回调
 * Created by win7 on 2017-04-14.
 */

public interface ProgressCallBack extends ResponseCallback {
    void OnStart();//在原有的基础上添加开始结构，区别于rxjava的onstart （线程机制）
}
