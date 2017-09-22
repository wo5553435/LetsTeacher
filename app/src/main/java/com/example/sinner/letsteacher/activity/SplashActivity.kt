package com.example.sinner.letsteacher.activity

import android.animation.Animator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.ViewAnimationUtils
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.utils.SpUtil

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import butterknife.BindView
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobConfig
import com.example.sinner.letsteacher.entity.LoginUserEntity
import com.example.sinner.letsteacher.utils.Logs
import rx.Observable
import rx.Subscriber
import kotlinx.android.synthetic.main.layout_activity_splash.*

/**
 * Created by win7 on 2017-06-13.
 */

class SplashActivity : BasicActivity(), View.OnClickListener {

    internal var animator: Animator?=null
    private val isHighVersion = false

    val user :String by SpUtil(this,"user","")


    override fun getContentLayout(): Int {
        return R.layout.layout_activity_splash
    }

    override fun initGui() {
    }


    override fun initData() {
        test()
        testWith()
    }

    fun testWith(){
        val a="string".run {
            if(contains("s")) return@run "1"
            "s"
        }
        Logs.e("a","--"+a)

        val b=run {
            if((1+1)==2) 3
            "1"
        }
        Logs.e("b",""+b)
    }

    private fun test() {
        val config = BmobConfig.Builder(activity)
                //        //设置appkey 建议自己去bmob
                .setApplicationId("0356f50f31a983aeae8156765c7a3284")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build()
        Bmob.initialize(config)
    }

    private fun showAnimation() {
        img_splash_icon!!.alpha = 0.2f
        ViewCompat.animate(tv_splash_appname).setDuration(1500).translationX(1.0f).alpha(1.0f).setInterpolator(FastOutSlowInInterpolator()).start()
        ViewCompat.animate(img_splash_icon).setDuration(1500).translationY(1.0f).alpha(1.0f).setInterpolator(FastOutSlowInInterpolator()).setListener(object : ViewPropertyAnimatorListener {
            override fun onAnimationStart(view: View) {

            }

            override fun onAnimationEnd(view: View) {
                Handler().postDelayed({ startHomePage() }, 1000)
            }

            override fun onAnimationCancel(view: View) {

            }
        }).start()
    }

    private var hasAnimationStarted: Boolean = false

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && !hasAnimationStarted) {
            tv_splash_appname?.alpha = 0.0f
            tv_splash_appname?.translationX = -100f
            img_splash_icon!!.alpha = 0.0f
            img_splash_icon!!.translationY = -20f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                initAnimation()
            } else {
                showAnimation()
            }
            hasAnimationStarted = true
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun initAnimation() {
        animator = ViewAnimationUtils.createCircularReveal(layout_splash_root,
                0, 0,
                0f, (layout_splash_root!!.height * 3 / 2).toFloat())
                .setDuration(1500)
        animator?.interpolator = FastOutSlowInInterpolator()
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                showAnimation()
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
        animator?.start()

    }


    override fun initAction() {
        tv_splash_appname?.setOnClickListener(this)
    }

    private fun startHomePage() {
        //HomeActivity
        var intent :Intent?=null
        if("".equals(user)){
           intent = Intent(activity, LoginAc::class.java)
        }else{
            LoginUserEntity.userid=user;
            intent = Intent(activity, HomeActivity::class.java)
        }
        //        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        //            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, tv_splash_appname, "shareView").toBundle());

        //            View statusBar = findViewById(android.R.id.statusBarBackground);
        //            View navigationBar = findViewById(android.R.id.navigationBarBackground);
        //            View mSharedElement=tv_splash_appname;
        //            List<Pair<View, String>> pairs = new ArrayList<>();
        //            pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
        //           // pairs.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
        //            pairs.add(Pair.create(mSharedElement, mSharedElement.getTransitionName()));
        //
        //            Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
        //                    pairs.toArray(new Pair[pairs.size()])).toBundle();
        //            startActivity(intent, options);
        //        }else{
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        //        }


        Handler().postDelayed({ finish() }, 2000)
        //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, shareView, "shareView").toBundle());
    }

    override fun onClick(view: View) {
        showAnimation()
    }
}
