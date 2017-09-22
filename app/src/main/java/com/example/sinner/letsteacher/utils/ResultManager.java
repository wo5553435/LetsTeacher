package com.example.sinner.letsteacher.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 标准返回结果管理 
 */
public class ResultManager<T> {

	private T entity;

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public T resolveEntity(Context context, Class<T> classOfT, String jsonStr, String describeStr) {
//		Logs.i("TAB", "请求类型：" + describeStr);
//		Logs.i("TAB", "第一层返回信息：" + jsonStr);
		try {
			Gson gson = new Gson();
			JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
			T result = gson.fromJson(jsonObject, classOfT);
			return result;
		} catch (Exception e) {
			//ToastTools.showShort(context, context.getResources().getString(R.string.dialog_qingshaohouchongshi));
			return null;
		}
	}
	
	public T resolveEntity(Class<T> classOfT, String jsonStr, String describeStr){
		try {
			Gson gson = new Gson();
			JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
			T result = gson.fromJson(jsonObject, classOfT);
			return result;
		} catch (Exception e) {
			return null;
		}
	}
}