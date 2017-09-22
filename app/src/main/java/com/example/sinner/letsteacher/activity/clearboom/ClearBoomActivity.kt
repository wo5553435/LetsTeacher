package com.example.sinner.letsteacher.activity.clearboom

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.activity.BasicActivity
import com.example.sinner.letsteacher.kotterknife.*
import kotlinx.android.synthetic.main.activity_clear_boom.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class ClearBoomActivity : BasicActivity() {
    override fun getContentLayout() = R.layout.activity_clear_boom

    override fun initGui() {
        tv_spendtime.text = "000"
    }

    override fun initData() {
    }

    override fun initAction() {
    }

    fun startcount() {
//        var s=rx.Observable.just("")
//                .subscribeOn(rx.schedulers.Schedulers.io())
////                .compose(RxLifecycle.bind<>(activity))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe()
    }

}
