package com.pigcms.library.utils;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.telephony.TelephonyManager;

/**
 * 通用方法封装
 * @Description 
 * @Author lewis(lgs@yitong.com.cn) 2014-3-3 下午1:25:28
 * @Class CommonUtil
 * Copyright (c) 2014 Shanghai P&C Information Technology Co.,Ltd. All rights reserved.
 */
public class CommonUtil {

	// 获取根目录路径
	public static String getSDPath() {
		boolean hasSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		// 如果有sd卡，则返回sd卡的目录
		if (hasSDCard) {
			return Environment.getExternalStorageDirectory().getPath();
		} else
			// 如果没有sd卡，则返回存储目录
			return Environment.getDownloadCacheDirectory().getPath();
	}
		
	/****
	 * 获取手机序列号
	 */
	public static String getDeviceId(Context ctx) {
		String deviceID = "";
		TelephonyManager tm = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getDeviceId() != null) {
			deviceID = tm.getDeviceId();
		} else {
			deviceID = getUUID(ctx);
		}
		return deviceID;
	}
	
	/**
	 * 得到全局唯一UUID
	 */
	public static final String MOBILE_SETTING = "MOBILE_SETTING";
	public static final String MOBILE_UUID = "MOBILE_UUID";

	public static String getUUID(Context context) {
		SharedPreferences mShare = context.getSharedPreferences(MOBILE_SETTING,
				0);
		String uuid = "";
		if (mShare != null
				&& !StringTools.isEmpty(mShare.getString(MOBILE_UUID, ""))) {
			uuid = mShare.getString(MOBILE_UUID, "");
		}
		if (StringTools.isEmpty(uuid)) {
			uuid = UUID.randomUUID().toString();
			mShare.edit().putString(MOBILE_UUID, uuid).commit();
		}
		// Log.d("TAG", "getUUID : " + uuid);
		return uuid;
	}

	/**
	 * 取得应用的版本号
	 */
	public static String getAPKVersion(Context ctx) {
		PackageManager packageManager = ctx.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					ctx.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static int transColor(String hexColor) {
		int color = Integer.parseInt(hexColor, 16);

		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = color & 0xFF;

		return (0xFF << 24) | (red << 16) | (green << 8) | blue;
	}
	
	/**
	 * 判断字符串是否不为空
	 * 
	 * @param value
	 * @return 字符串不为空返回true，否则返回false
	 */
	public static boolean isStringNotEmpty(String value) {

		if (value != null && !"".equals(value)) {
			return true;
		} else {
			return false;
		}
	}

	//
	/**
	 * 判断字符串是否空
	 * 
	 * @param value
	 * @return 字符串不为空返回true，否则返回false
	 */
	public static boolean isStringEmpty(String value) {

		return !isStringNotEmpty(value);
	}

}
