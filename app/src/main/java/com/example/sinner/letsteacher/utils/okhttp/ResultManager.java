package com.example.sinner.letsteacher.utils.okhttp;

import android.content.Context;

import com.example.sinner.letsteacher.utils.Logs;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
//			ToastUtil.getInstance(context).showToast("请稍后重试");
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
			Logs.e("entity is badconversion ", "--");
			e.printStackTrace();
			return null;
		}
	}

	public T resolveEntity(Class<T>classofT, JsonElement element){
		Gson gson = new Gson();
		try {
			T result = gson.fromJson(element, classofT);
			return result;
		} catch (Exception e) {
			Logs.e("entity is badconversion ", "--");
			e.printStackTrace();
//			T result = resolveEntity(classofT,element.getAsString(),"");
//			return result;
			return null;
		}
	}
}