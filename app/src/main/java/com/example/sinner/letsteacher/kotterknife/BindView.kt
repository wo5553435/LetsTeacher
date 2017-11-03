package com.example.sinner.letsteacher.kotterknife

import android.support.v7.app.AppCompatActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by sinner on 2017-10-09.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
class BindV<T> (val id:Int) :ReadOnlyProperty<AppCompatActivity,T>{
    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        return thisRef.findViewById(id) as T
    }
}