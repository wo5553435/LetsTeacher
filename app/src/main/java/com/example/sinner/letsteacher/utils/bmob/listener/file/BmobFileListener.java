package com.example.sinner.letsteacher.utils.bmob.listener.file;

/**
 * Created by win7 on 2017-02-22.
 */

public abstract class BmobFileListener {

    public abstract  void OnSuccess(String filepath);
    public abstract  void OnFail(String code,String message);
}
