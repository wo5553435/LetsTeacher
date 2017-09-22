package com.example.sinner.letsteacher.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.app.Activity
import android.app.LoaderManager.LoaderCallbacks

import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask

import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo

import java.util.ArrayList

import com.example.sinner.letsteacher.R

import android.Manifest.permission.READ_CONTACTS
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Rect
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import android.view.View.*
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.*
import com.example.sinner.letsteacher.entity.LoginUserEntity
import com.example.sinner.letsteacher.entity.UserVo
import com.example.sinner.letsteacher.utils.Logs
import com.example.sinner.letsteacher.utils.SpUtil
import com.example.sinner.letsteacher.utils.SuperToastUtil
import com.example.sinner.letsteacher.utils.bmob.BmobUtil
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobQueryListener
import kotlinx.android.synthetic.main.activity_login2.*;

/**
 * A login screen that offers login via email/password.
 */
class LoginAc : BasicActivity() {
    var user :String by SpUtil(this,"user","")
    var params_width:Int=0
    var params:LinearLayout.LayoutParams?=null
    var btn_login:Button?=null
    override fun getContentLayout()=R.layout.activity_login2

    override fun initGui() {
//        testWith()
        btn_login=findViewById(R.id.login_btn_login) as Button
//        val recyclerView = findViewById<TextView>(R.id.recycler_view)
    }

    override fun initData() {
        registerKeyboardListener()
        if(intent.hasExtra("clear")){
            user=""
        }
    }

    override fun initAction() {
        sign_up.setOnClickListener{ startActivity(Intent(this@LoginAc,RegisterAc::class.java))}
        login_btn_login.setOnClickListener{
            loginAction();
            }
    }




    fun changeBtnStatu(showflag :Boolean ){
        var va:ValueAnimator
        if(showflag){
            va=ValueAnimator.ofFloat(0.15f,1f)
            va.startDelay=500
        }else va=ValueAnimator.ofFloat(1f,0.15f)
        va.setDuration(500)
        va.interpolator=AnticipateOvershootInterpolator()
        if(params_width==0){
            params_width= layout_loginarea.width
            params=layout_loginarea.layoutParams as LinearLayout.LayoutParams
        }
        va.addUpdateListener { valueAnimator ->
            Logs.e("width:"+params_width,"--current width-"+ (valueAnimator.animatedValue as Float*params_width));
            params?.width = (valueAnimator.animatedValue as Float*params_width).toInt()
            layout_loginarea.setLayoutParams(params)
            login_btn_login.setTextSize(18*valueAnimator.animatedValue as Float)
        }
        va.addListener(object :Animator.AnimatorListener{
            override fun onAnimationRepeat(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                if(showflag){
                }else{
                    login_btn_login.visibility= GONE
                    checkLogin()
                }
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationStart(p0: Animator) {
                if(showflag){
                    login_btn_login.visibility= VISIBLE
                }else{
                }
            }

        })
        va.start()
    }

    /**
     * 等待层的展示动画
     *
     */
    private fun showwait(showflag: Boolean){
        if(showflag) {
            login_wait_dialog.visibility= VISIBLE
            var v = ViewCompat.animate(login_wait_dialog).setDuration(600).setStartDelay(200).alpha(1.0f)

            v.start()
        }
        else {
            var v=    ViewCompat.animate(login_wait_dialog).setDuration(500).alpha(0.5f)
            v.setListener(object :ViewPropertyAnimatorListener{
                override fun onAnimationEnd(view: View?) {
                    login_wait_dialog.visibility= INVISIBLE
                }

                override fun onAnimationCancel(view: View?) {
                }

                override fun onAnimationStart(view: View?) {
                }
            })
            v.start()
        }
    }

    /**
     * 登录操作
     */
    private fun loginAction() {
        if(login_username.text.toString().length==0){
            SuperToastUtil.getInstance(activity).showToast("账号没输入~");return
        }
        if(login_password.text.toString().length==0){
            SuperToastUtil.getInstance(activity).showToast("密码没输入~");return
        }
        changeBtnStatu(false);
        showwait(true)
    }

    fun checkLogin(){
        var User :UserVo?=null
        val param:Map<String,String> = mapOf("username" to login_username.text.toString())
        BmobUtil.getInstance().SearchData(User,param,object : BmobQueryListener<UserVo>(){
            override fun OnSuccess(obj: UserVo?) {
            }

            override fun OnSuccess(objs: MutableList<UserVo>?) {
                if(objs?.size==1){
                    if(!login_password.text.toString().equals(objs[0].password)){
                        SuperToastUtil.getInstance(activity).showToast("密码不正确哦~!")
                        changeBtnStatu(true);
                        showwait(false)
                        return
                    }
                    user=objs[0].id
                    LoginUserEntity.userid=objs[0].id;
                    SuperToastUtil.getInstance(activity).showToast("欢迎回来!"+objs[0].username)
                    startActivity(Intent(activity,HomeActivity::class.java))
                    finish()
                }else if(objs?.size!! >=2){
                    SuperToastUtil.getInstance(activity).showToast("抱歉!该账号有点问题!请联系管理员qq563424276")
                    changeBtnStatu(true);
                    showwait(false)
                }else {
                    SuperToastUtil.getInstance(activity).showToast("账号未找到~!")
                    changeBtnStatu(true);
                    showwait(false)
                }
            }

            override fun OnFail(code: String?, error: String?) {
                SuperToastUtil.getInstance(activity).showToast("抱歉!"+error)
                changeBtnStatu(true);
                showwait(false)
            }

        })
    }
    fun testWith(){
       val a= with(ArrayList<Int>() ){
            add(1)
            remove(1)
            return@with
            1
        }
        Logs.e("a","--"+a)
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterKeyboardListener()
    }


    fun registerKeyboardListener(){
        val rootView = window.decorView.findViewById(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            Logs.e("--","onGlobalLayout")
            if (isKeyboardShown(rootView)) {
                Logs.e("--","软键盘弹起")
               span1.setVisibility(View.GONE)
               span2.setVisibility(View.GONE)
            } else {
                Logs.e("--","软键盘未弹起")
              span1.setVisibility(View.INVISIBLE)
              span2.setVisibility(View.INVISIBLE)
            }
        }
    }

    private fun unRegisterKeyboardListener() {
        val rootView = window.decorView.findViewById(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(null)
    }

    private fun isKeyboardShown(rootView: View): Boolean {
        val softKeyboardHeight = 100
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val dm = rootView.resources.displayMetrics
        val heightDiff = rootView.bottom - r.bottom
        return heightDiff > softKeyboardHeight * dm.density
    }
}

