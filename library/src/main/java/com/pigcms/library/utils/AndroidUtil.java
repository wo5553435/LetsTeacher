package com.pigcms.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * android客户端工具类
 */
public class AndroidUtil {

	/**
	 * 设备唯一号存储
	 */
	public static final String MOBILE_SETTING = "SYT_MOBILE_SETTING";

	/**
	 * 设备唯一表示
	 */
	public static final String MOBILE_UUID = "SYT_MOBILE_UUID";

	/**
	 * deviceID的组成为：渠道标志[wifi:wifi|imei:imei|sn:sn] 
	 * 如果wifi imei sn 标识都获取不到 则 通过UUID 生成随机码 缓存在客户端作为机器唯一标识
	 * 返回 取到标志[id:id]
	 * 
	 * 渠道标志为： 1，andriod（a）
	 * 
	 * @Description
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		StringBuilder deviceId = new StringBuilder();
		// 渠道标志 a
		//deviceId.append("a");
		try {
			deviceId.append("[");
			// wifi mac地址
//			WifiManager wifi = (WifiManager) context
//					.getSystemService(Context.WIFI_SERVICE);
//			WifiInfo info = wifi.getConnectionInfo();
//			String wifiMac = info.getMacAddress();
//			if (StringTools.isNotEmpty(wifiMac)) {
//				deviceId.append("wifi:");
//				deviceId.append(wifiMac);
//				deviceId.append("|");
//				Logs.d("getDeviceId : ", deviceId.toString());
//			}

			// IMEI（imei）
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String imei = tm.getDeviceId();
			if (!TextUtils.isEmpty(imei)) {
				//deviceId.append("imei:");
				deviceId.append(imei);
				//deviceId.append("|");
				Log.d("getDeviceId : ", deviceId.toString());
			}

			// 序列号（sn）
//			String sn = tm.getSimSerialNumber();
//			if (StringTools.isNotEmpty(sn)) {
//				deviceId.append("sn:");
//				deviceId.append(sn);
//				deviceId.append("|");
//				Logs.d("getDeviceId : ", deviceId.toString());
//			}
			/**
			 * 判断是否有拼接到 wifi | imei | sn 如果长度小于3 代表没有， 则 生成随机码
			 */
			if (TextUtils.isEmpty(deviceId.toString())||deviceId.toString().length()<3) {
				// 如果上面都没有， 则生成一个id：随机码
				String uuid = getUUID(context);
				if (!TextUtils.isEmpty(uuid)) {
					deviceId.append("id:");
					deviceId.append(uuid);
					Log.d("getDeviceId : ", deviceId.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			deviceId.append("id:").append(getUUID(context));
		}

		deviceId.append("]");
		Log.d("getDeviceId : ", deviceId.toString());

		return deviceId.toString();

	}

	/**
	 * 得到全局唯一UUID
	 */
	public static String getUUID(Context context) {
		SharedPreferences mShare = context.getSharedPreferences(MOBILE_SETTING,
				0);
		String uuid = "";
		if (mShare != null
				&& !TextUtils.isEmpty(mShare.getString(MOBILE_UUID, ""))) {
			uuid = mShare.getString(MOBILE_UUID, "");
		}
		if (TextUtils.isEmpty(uuid)) {
			uuid = UUID.randomUUID().toString();
			mShare.edit().putString(MOBILE_UUID, uuid).commit();
		}
		Log.d("getUUID", "getUUID : " + uuid);
		return uuid;
	}

	public static String getClientDeviceInfo(Context ctx) {
		String deviceID = "";
		String serial = "";
		deviceID = getDeviceId(ctx);
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class);
			serial = (String) get.invoke(c, "ro.serialno");
		} catch (Exception e) {
			Log.e("TAG", "get the system sn ERROR!", e);
		}
		Log.d("serial", "deviceID:" + deviceID);
		String buildVersion = android.os.Build.VERSION.RELEASE;
		return deviceID + "|android" + "|android|" + buildVersion + "|android";
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}


	/**
     * sp 转 px
	 * @param context
     * @param spVal
     * @return
     */
	public static int sp2px(Context context, float spVal)

	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, context.getResources().getDisplayMetrics());

	}

	/**
	 * px转sp
	 * @param pxVal
	 * @return
	 */

	public static float px2sp(Context context, float pxVal)
	{
		return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
	}


	/**
	 * 登录检测输入的手机号
	 * @param phoneNum
	 * @return
	 */
	public static boolean checkPhoneNum(String phoneNum){
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$");
		Matcher m = p.matcher(phoneNum);
		return m.matches();
	}
	/*
	 * 取得操作系统版本号
	 */
	public static String getOSVersion(Context ctx) {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 取得应用的版本号
	 */
	public  static String getAPKVersion(Context ctx) {
		PackageManager packageManager = ctx.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					ctx.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

}
