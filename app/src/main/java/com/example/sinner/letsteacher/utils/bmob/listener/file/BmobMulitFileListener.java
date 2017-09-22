package com.example.sinner.letsteacher.utils.bmob.listener.file;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by win7 on 2017-02-22.
 */

public abstract class BmobMulitFileListener {
    public abstract  void OnSuccess(List<BmobFile> files,List<String> urls,int failcount);

    public abstract  void OnProgress(int current,int total);

    public abstract  void OnFail(String code,String message);
}
