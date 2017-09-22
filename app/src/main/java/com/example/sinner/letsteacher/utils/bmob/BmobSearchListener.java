package com.example.sinner.letsteacher.utils.bmob;

import com.example.sinner.letsteacher.utils.bmob.listener.data.BaseBmobListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by win7 on 2016-12-27.
 */

public abstract class BmobSearchListener<T> implements BaseBmobListener {
    private Class<T> classoft;
    ArrayList<T> results;

    public BmobSearchListener(Class<T> t){
        classoft=t;
    }
    public abstract void OnSuccess(ArrayList<T> arrayList);

    public abstract  void OnFail(String errorcode,String errormsg);

    @Override
    public void onDone(JSONArray ary, BmobException e) {
        if(e==null){//没有出错的情况
            if(ary instanceof JSONArray){//返回对象为数组
                 results=new ArrayList<T>();;
                if(ary!=null&&ary.length()>0){

                    for(int i=0;i<ary.length();i++) {
                        try {
                            String objstr = ary.getJSONObject(i).toString();
                            T temp=convert(objstr);
                            if(temp!=null){
                                results.add(temp);
                            }else break;
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            OnFail("0","数据返回解析异常!");
                        }
                    }
                }
                OnSuccess(results);
            }else{
                OnFail("510","数据返回格式异常!");
            }

        }else{
            OnFail(""+e.getErrorCode(),e.getMessage());
        }
    }



    private T convert(String jsonStr){
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
            T result = gson.fromJson(jsonObject, classoft);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onDone(BmobObject object, BmobException e) {

    }

    @Override
    public void onDone(String s, BmobException e) {

    }
}
