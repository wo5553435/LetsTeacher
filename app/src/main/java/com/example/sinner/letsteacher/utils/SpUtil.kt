package com.example.sinner.letsteacher.utils

import android.content.Context
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**sharedpreferences管理工具
 * Created by sinner on 2017-09-08.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
class SpUtil<T>(val context: Context, val key: String, val defalut: T) : ReadWriteProperty<Any?, T> {
    val sp_name = "myapplication"
    var res: Any? = null
    val sp by lazy {
        context.getSharedPreferences(sp_name, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        with(sp) {
            res = when (defalut) {
                is Long -> getLong(key, defalut)
                is String -> getString(key, defalut)
                is Int -> getInt(key, defalut)
                is Boolean -> getBoolean(key, defalut)
                is Float -> getFloat(key, defalut)
                else -> null//非基本类直接返回null
            }
        }
        return res as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        with(sp.edit()) {
            when (value) {
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                else -> putString(key, "")//非基本类保存成空字符串
            }
        }.apply()
    }

    fun clearPrefences(){
        sp.edit().clear().commit()
    }

    fun clearPrefences(key:String){
        sp.edit().remove(key).commit()
    }

}