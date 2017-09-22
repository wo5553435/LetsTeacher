package com.example.sinner.letsteacher.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.TelephonyManager
import android.util.Log
import android.util.TypedValue
import java.util.*
import java.util.regex.Pattern

/**
 * Created by win7 on 2017-03-04.
 */

class AndroidUtils(val Name: String) {
    companion object {


        /**
         * 设备唯一号存储
         */
        val MOBILE_SETTING = "SYT_MOBILE_SETTING"

        /**
         * 设备唯一表示
         */
        val MOBILE_UUID = "SYT_MOBILE_UUID"

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
        fun getDeviceId(context: Context): String {
            val deviceId = StringBuilder()
            // 渠道标志 a
            //deviceId.append("a");
            try {
                deviceId.append("[")
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
                val tm = context
                        .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val imei = tm.deviceId
                if (!StringTools.isEmpty(imei)) {
                    //deviceId.append("imei:");
                    deviceId.append(imei)
                    //deviceId.append("|");
                    Logs.d("getDeviceId : ", deviceId.toString())
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
                if (StringTools.isEmpty(deviceId.toString()) || deviceId.toString().length < 3) {
                    // 如果上面都没有， 则生成一个id：随机码
                    val uuid = getUUID(context)
                    if (!StringTools.isEmpty(uuid)) {
                        deviceId.append("id:")
                        deviceId.append(uuid)
                        Logs.d("getDeviceId : ", deviceId.toString())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                deviceId.append("id:").append(getUUID(context))
            }

            deviceId.append("]")
            Logs.d("getDeviceId : ", deviceId.toString())

            return deviceId.toString()

        }

        /**
         * 得到全局唯一UUID
         */
        fun getUUID(context: Context): String {
            val mShare = context.getSharedPreferences(MOBILE_SETTING,
                    0)
            var uuid: String? = ""
            if (mShare != null && !StringTools.isEmpty(mShare.getString(MOBILE_UUID, ""))) {
                uuid = mShare.getString(MOBILE_UUID, "")
            }
            if (StringTools.isEmpty(uuid)) {
                uuid = UUID.randomUUID().toString()
                mShare!!.edit().putString(MOBILE_UUID, uuid).commit()
            }
            Logs.d("getUUID", "getUUID : " + uuid!!)
            return uuid
        }

        fun getClientDeviceInfo(ctx: Context): String {
            var deviceID = ""
            var serial = ""
            deviceID = getDeviceId(ctx)
            try {
                val c = Class.forName("android.os.SystemProperties")
                val get = c.getMethod("get", String::class.java)
                serial = get.invoke(c, "ro.serialno") as String
            } catch (e: Exception) {
                Log.e("TAG", "get the system sn ERROR!", e)
            }

            Log.d("serial", "deviceID:" + deviceID)
            val buildVersion = android.os.Build.VERSION.RELEASE
            return "$deviceID|android|android|$buildVersion|android"
        }

        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        fun px2dip(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }


        /**
         * sp 转 px
         * @param context
         * @param spVal
         * @return
         */
        fun sp2px(context: Context, spVal: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    spVal, context.resources.displayMetrics).toInt()

        }

        /**
         * px转sp
         * @param pxVal
         * @return
         */

        fun px2sp(context: Context, pxVal: Float): Float {
            return pxVal / context.resources.displayMetrics.scaledDensity
        }


        /**
         * 登录检测输入的手机号
         * @param phoneNum
         * @return
         */
        fun checkPhoneNum(phoneNum: String): Boolean {
            val p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$")
            val m = p.matcher(phoneNum)
            return m.matches()
        }

        /*
         * 取得操作系统版本号
         */
        fun getOSVersion(ctx: Context): String {
            return android.os.Build.VERSION.RELEASE
        }

        /**
         * 取得应用的版本号
         */
        fun getAPKVersion(ctx: Context): String {
            val packageManager = ctx.packageManager
            try {
                val packageInfo = packageManager.getPackageInfo(
                        ctx.packageName, 0)
                return packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return ""
        }

//    fun CallPhone(activity: Activity, number: String) {
//        try {
//            val mDialog = MyDialog(activity,
//                    R.style.MyDialog)
//            mDialog.setTextTitle(activity.resources.getString(
//                    R.string.dialog_wenxintishi))
//            mDialog.setTextContent("是否拨打电话:" + number)
//            mDialog.setCancelable(false)
//            mDialog.setOnlyOk(false)
//            mDialog.setOnResultListener(object : MyDialog.OnResultListener() {
//
//                fun Ok() {
//                    mDialog.dismiss()
//                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number))
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    activity.startActivity(intent)
//                }
//
//                fun Cancel() {
//                    mDialog.dismiss()
//                }
//            })
//            mDialog.show()
//
//
//        } catch (e: Exception) {
//            SuperToastUtil.getInstance(activity).showToast("错误的电话格式")
//        }
//
//    }


        /**
         * @Title:main
         * @Description:生成随机颜色
         * @param:@param args
         * @return: void
         * @throws
         */
        fun  getRandomColor() :String{
            //红色
            var red: String
            //绿色
            var green: String
            //蓝色
            var blue: String
            //生成随机对象
            val random = Random()
            //生成红色颜色代码
            red = Integer.toHexString(random.nextInt(256)).toUpperCase()
            //生成绿色颜色代码
            green = Integer.toHexString(random.nextInt(256)).toUpperCase()
            //生成蓝色颜色代码
            blue = Integer.toHexString(random.nextInt(256)).toUpperCase()

            //判断红色代码的位数
            red = if (red.length == 1) "0" + red else red
            //判断绿色代码的位数
            green = if (green.length == 1) "0" + green else green
            //判断蓝色代码的位数
            blue = if (blue.length == 1) "0" + blue else blue
            //生成十六进制颜色值
            val color = "#" + red + green + blue
            return color
        }
    }

}