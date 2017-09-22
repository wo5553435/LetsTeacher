package com.example.sinner.letsteacher.utils.bmob.listener.data;

import com.example.sinner.letsteacher.utils.bmob.listener.data.BaseBmobListener;

import org.json.JSONArray;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by win7 on 2016-12-28.
 */

public abstract class BmobAddOrUpdateListener implements BaseBmobListener {

    public abstract void OnSuccess(String backstr);

    public abstract void OnFail(String errormsg);


    @Override
    public void onDone(JSONArray array, BmobException e) {

    }

    @Override
    public void onDone(BmobObject object, BmobException e) {

    }

    @Override
    public void onDone(String s, BmobException e) {
        if(e!=null){//有错误的返回
            OnFail(e.getMessage());
        }else if(s.startsWith("Error")){//自定义错误
            OnFail(s.substring(5,s.length()-1));
        }else{
            if(s=="")
                OnSuccess("操作成功");
            else{
                OnSuccess(s);
            }
        }
    }
}
