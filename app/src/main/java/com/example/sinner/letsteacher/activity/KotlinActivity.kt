package com.example.sinner.letsteacher.activity

import android.os.Bundle
import android.app.Activity
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder

import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.utils.Logs
import java.lang.Integer.parseInt

abstract class KotlinActivity : Activity() {

    var unbind :Unbinder ?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        unbind = ButterKnife.bind(this)
        initView()
        initAction()
        initData()
    }

    fun initView(){

    }

    fun initAction(){

    }

    fun initData(){
    }

    override fun onDestroy() {
        super.onDestroy()
        unbind?.unbind();
    }

    private fun Setdata(c: List<String>) {
        for (i in c.indices) {
            Logs.e("i:" + i, c[i])
        }
    }

     fun Strisempty (str: String)=if(str==null) true else false

     fun StringisInt(str:String):Int? {
         when (parseInt(str)) {
             1, 4 -> print("the value is zero or one")
             else -> print("")
         }
        return parseInt(str)
     }

    fun parseInt(str: String): Int? {
        return str.toIntOrNull()
    }


}
