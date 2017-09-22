package com.example.sinner.letsteacher.utils.bmob.listener.file;

/**
 * Created by win7 on 2017-02-22.
 */

public abstract class BmobDownListener {
    public abstract  void OnSuccess(String savepath);

    public abstract  void OnFail(int code,String message);

    public abstract void OnProgress(int curent,int total);

}

