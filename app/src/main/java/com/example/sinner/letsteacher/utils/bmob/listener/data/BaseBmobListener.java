package com.example.sinner.letsteacher.utils.bmob.listener.data;

import org.json.JSONArray;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by win7 on 2016-12-27.
 * 其实吧 这个公共基础接口并没有什么用，bmob给我们的接口并不是有统一规范
 */

public   interface BaseBmobListener {

     void onDone(BmobObject object, BmobException e);

    void onDone(JSONArray array,BmobException e);

    void onDone(String s,BmobException e);

}
