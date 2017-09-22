package com.example.sinner.letsteacher.utils.bmob.listener.data;

import com.example.sinner.letsteacher.utils.bmob.listener.data.BaseBmobListener;

import org.json.JSONArray;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by win7 on 2016-12-27.
 */

public abstract class BmobQueryListener<T> extends FindListener<T>  {


    @Override
    public void done(List<T> list, BmobException e) {
        if(e!=null)
            OnFail(""+e.getErrorCode(),e.getMessage());
        else{
            OnSuccess(list);
        }

    }



    public abstract  void OnSuccess(T object);

    public abstract  void OnSuccess(List<T> objects);

    public abstract void OnFail(String code,String error);

}
