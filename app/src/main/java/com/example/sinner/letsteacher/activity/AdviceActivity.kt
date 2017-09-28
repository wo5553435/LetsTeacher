package com.example.sinner.letsteacher.activity

import android.view.View
import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.entity.AdviceVo
import com.example.sinner.letsteacher.entity.LoginUserEntity
import com.example.sinner.letsteacher.interfaces.MyInputTextWatcher
import com.example.sinner.letsteacher.utils.Logs
import com.example.sinner.letsteacher.utils.SuperToastUtil
import com.example.sinner.letsteacher.utils.bmob.BmobUtil
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobAddOrUpdateListener
import com.pigcms.library.android.interfaces.NoSpecialCodeListener
import kotlinx.android.synthetic.main.activity_advice.*;
import kotlinx.android.synthetic.main.layout_title_blue.*;
import rx.Observable
import rx.Observer
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class AdviceActivity : BasicActivity() {
    lateinit var sub :Subscriber<Long>
    var text=StringBuffer("正在请求")

    var data:AdviceVo= AdviceVo()
    override fun getContentLayout() = R.layout.activity_advice
    override fun initGui() {
        title_lin_back.visibility = View.VISIBLE
        title_text.text = "意见提交"
    }

    override fun initData() {
        initSub();
    }

    override fun initAction() {
        ed_advice_edit.setFilters(NoSpecialCodeListener.getInstance().Filter())
        val inputTextWatcher = MyInputTextWatcher(activity, activity, null, ed_advice_edit, tv_advice_count)
        ed_advice_edit.addTextChangedListener(inputTextWatcher)
        btn_advice_commit.setOnClickListener{sendAdvice()}
    }

    fun initSub(){
        sub=object: Subscriber<Long>() {

            override fun onError(p0: Throwable) {
                Logs.e("sth is wrong","--"+p0.message)
            }

            override fun onNext(p0: Long) {
                if( btn_advice_commit.text.length>7)  text=StringBuffer("正在请求")
                btn_advice_commit.text= text.apply{text.append(".")}

            }

            override fun onCompleted() {
                Logs.e("onCompleted","--")
            }
        }
    }

    /**
     * 发送建议
     */
    private fun sendAdvice() {
        data.userid=LoginUserEntity.userid
        data.advice=ed_advice_edit.text.toString().trim()
        loading()
        BmobUtil.getInstance().addData(data,object :BmobAddOrUpdateListener(){
            override fun OnSuccess(backstr: String?) {
                SuperToastUtil.getInstance(activity).showToast("提交成功~!谢谢您的宝贵建议!")
                ed_advice_edit.setText("");
                stop()
            }

            override fun OnFail(errormsg: String?) {
                stop()
                SuperToastUtil.getInstance(activity).showToast(errormsg)
            }
        })
    }

    fun loading() {
        btn_advice_commit.isEnabled=false
        Observable.interval(0, 300, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub.apply { if(sub.isUnsubscribed) initSub() });
    }

    fun stop(){
        if(!sub.isUnsubscribed)
            sub.unsubscribe()
        btn_advice_commit.text="提 交"
        btn_advice_commit.isEnabled=true
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }
}
