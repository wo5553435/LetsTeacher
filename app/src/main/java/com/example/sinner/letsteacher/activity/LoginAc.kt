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

import com.example.sinner.letsteacher.R

import android.Manifest.permission.READ_CONTACTS
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.ViewPropertyAnimatorUpdateListener
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import android.view.View.*
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.sinner.letsteacher.entity.LoginUserEntity
import com.example.sinner.letsteacher.entity.UserVo
import com.example.sinner.letsteacher.utils.Logs
import com.example.sinner.letsteacher.utils.SpUtil
import com.example.sinner.letsteacher.utils.SuperToastUtil
import com.example.sinner.letsteacher.utils.bmob.BmobUtil
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobQueryListener
import com.example.sinner.letsteacher.utils.share.ShareUtil
import kotlinx.android.synthetic.main.activity_login2.*;
import java.util.*

/**
 * A login screen that offers login via email/password.
 */
class LoginAc : BasicActivity() {
    var user: String by SpUtil(this, "user", "")
    var params_width: Int = 0
    var params: LinearLayout.LayoutParams? = null
    var btn_login: Button? = null
    override fun getContentLayout() = R.layout.activity_login2

    override fun initGui() {
//        testWith()
        btn_login = findViewById(R.id.login_btn_login) as Button
//        val recyclerView = findViewById<TextView>(R.id.recycler_view)
    }

    override fun initData() {
        registerKeyboardListener()
        if (intent.hasExtra("clear")) {
            user = ""
        }
     var a :List<Int> ?=null
//        a?.sort()
    }



    override fun initAction() {
        sign_up.setOnClickListener {
           // ShareUtil(activity).shareMsg("title","ceshi","")
            startActivity(Intent(this@LoginAc, RegisterAc::class.java))
        }
        login_btn_login.setOnClickListener {
            loginAction()
        }
    }


    fun changeBtnStatu(showflag: Boolean) {
        var va: ValueAnimator
        if (showflag) {
            va = ValueAnimator.ofFloat(0.15f, 1f)
            va.startDelay = 500
        } else va = ValueAnimator.ofFloat(1f, 0.15f)
        va.setDuration(500)
        va.interpolator = AnticipateOvershootInterpolator()
        if (params_width == 0) {
            params_width = layout_loginarea.width
            params = layout_loginarea.layoutParams as LinearLayout.LayoutParams
        }
        va.addUpdateListener { valueAnimator ->
            Logs.e("width:" + params_width, "--current width-" + (valueAnimator.animatedValue as Float * params_width));
            params?.width = (valueAnimator.animatedValue as Float * params_width).toInt()
            layout_loginarea.setLayoutParams(params)
            login_btn_login.setTextSize(18 * valueAnimator.animatedValue as Float)
        }
        va.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                if (showflag) {

                } else {
                    login_btn_login.visibility = GONE
                    login_wait_dialog.visibility= VISIBLE
                    checkLogin()
                }
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationStart(p0: Animator) {
                if (showflag) {
                    login_btn_login.visibility = VISIBLE
                    login_wait_dialog.visibility= GONE
                } else {
                }
            }

        })
        va.start()
    }

    fun test(){

    }

    interface  s{
        fun  dos();
         fun s();
    }

    /**
     * 等待层的展示动画
     *
     */
    private fun showwait(showflag: Boolean) {
        login_wait_dialog.visibility = if (showflag) VISIBLE else INVISIBLE

//        if (showflag) {
//            login_wait_dialog.visibility = VISIBLE
//            var v = ViewCompat.animate(login_wait_dialog).setDuration(600).setStartDelay(200)
//
//            v.start()
//        } else {
//            var v = ViewCompat.animate(login_wait_dialog).setDuration(500)
//            v.setListener(object : ViewPropertyAnimatorListener {
//                override fun onAnimationEnd(view: View?) {
//                    login_wait_dialog.visibility = INVISIBLE
//
//                }
//
//                override fun onAnimationCancel(view: View?) {
//                }
//
//                override fun onAnimationStart(view: View?) {
//                }
//            })
//            v.start()
//        }
    }

    inline fun test1(){
        listOf<String>("1","2","")
    }

    /**
     * 登录操作
     */
    private fun loginAction() {
        if (login_username.text.toString().length == 0) {
            SuperToastUtil.getInstance(activity).showToast("账号没输入~");return
        }
        if (login_password.text.toString().length == 0) {
            SuperToastUtil.getInstance(activity).showToast("密码没输入~");return
        }
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(window.decorView.windowToken,
                0)

        changeBtnStatu(false);
    }

    fun checkLogin() {
        showwait(true)
        var User: UserVo? = null
        val param: Map<String, String> = mapOf("username" to login_username.text.toString())
        BmobUtil.getInstance().SearchData(User, param, object : BmobQueryListener<UserVo>() {
            override fun OnSuccess(obj: UserVo?) {
            }

            override fun OnSuccess(objs: MutableList<UserVo>?) {
                if (objs?.size == 1) {
                    if (!login_password.text.toString().equals(objs[0].password)) {
                        SuperToastUtil.getInstance(activity).showToast("密码不正确哦~!")
                        showResult(false)
                        return
                    }
                    user = objs[0].id
                    LoginUserEntity.userid = objs[0].id;
                    SuperToastUtil.getInstance(activity).showToast("欢迎回来!" + objs[0].username)
                    showResult(true)
                    Handler().postDelayed({
                        startActivity(Intent(activity, HomeActivity::class.java))
                        finish()
                    }, 500)

                } else if (objs?.size!! >= 2) {//多账号匹配的问题 百分之九十是防多次点击没有生效
                    SuperToastUtil.getInstance(activity).showToast("抱歉!该账号有点问题!请联系管理员qq563424276")
                    showResult(false)
                } else {
                    SuperToastUtil.getInstance(activity).showToast("账号未找到~!")
                    showResult(false)

                }
            }

            override fun OnFail(code: String?, error: String?) {
                SuperToastUtil.getInstance(activity).showToast("抱歉!" + error)
                showwait(false)
            }

        })
    }

    /**
     * 展示登录结果
     * @param isRight ->是否是正确结果
     */
    fun showResult(isRight: Boolean) {
        img_login_error.visibility = if (isRight) View.GONE else View.VISIBLE
        img_login_right.visibility = if (isRight) VISIBLE else GONE
        showwait(false)
        val s = if (isRight) img_login_right else img_login_error
        s?.let {
            if (it.drawable is Animatable) (it.drawable as Animatable).start()
            else {
                it.scaleX = 0.0f;it.scaleY = 0.0f
                ViewCompat.animate(it).scaleX(1.0f).scaleY(1.0f).setInterpolator(FastOutSlowInInterpolator()).setDuration(500)
                        .withEndAction({
                            if (!isRight) {
                                changeBtnStatu(true)
                                ViewCompat.animate(it).scaleX(0.0f).scaleY(0.0f).setInterpolator(FastOutSlowInInterpolator()).setDuration(500).start()
                            }
                        })
                        .start()
            }
        }
//        }else{
//            img_login_error?.let{
//                it.scaleX = 0.0f;it.scaleY=0.0f
//                ViewCompat.animate(it).scaleX(1.0f).scaleY(1.0f).setInterpolator(FastOutSlowInInterpolator()).setDuration(400).start()
//
//            }
//        }
    }

    fun testWith() {
        val a = with(ArrayList<Int>()) {
            add(1)
            remove(1)
            return@with
            1
        }
        Logs.e("a", "--" + a)
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterKeyboardListener()
    }


    fun registerKeyboardListener() {
        val rootView = window.decorView.findViewById(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            Logs.e("--", "onGlobalLayout")
            if (isKeyboardShown(rootView)) {
                Logs.e("--", "软键盘弹起")
                span1.setVisibility(View.GONE)
                span2.setVisibility(View.GONE)
            } else {
                Logs.e("--", "软键盘未弹起")
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

