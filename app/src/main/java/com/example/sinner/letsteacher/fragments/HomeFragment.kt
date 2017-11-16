package com.example.sinner.letsteacher.fragments

import android.animation.Animator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.transition.Fade
import android.transition.Scene
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.activity.AddNewFileMkdirs
import com.example.sinner.letsteacher.activity.ShowNetPicActivity
import com.example.sinner.letsteacher.activity.TestActivity
import com.example.sinner.letsteacher.adapter.FileListAdapter
import com.example.sinner.letsteacher.entity.FileBean
import com.example.sinner.letsteacher.entity.ImageVo
import com.example.sinner.letsteacher.entity.LoginUserEntity
import com.example.sinner.letsteacher.utils.AndroidUtils
import com.example.sinner.letsteacher.utils.AnimatorUtil
import com.example.sinner.letsteacher.utils.Logs
import com.example.sinner.letsteacher.utils.StringTools
import com.example.sinner.letsteacher.utils.SuperToastUtil
import com.example.sinner.letsteacher.utils.bmob.BmobUtil
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobAddOrUpdateListener
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobQueryListener
import com.example.sinner.letsteacher.utils.file.FileUtils
import com.example.sinner.letsteacher.views.dialog.TextDialog

import java.io.File
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.TimeUnit

import butterknife.BindView
import butterknife.OnClick
import cn.bmob.v3.BmobObject
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.CountListener
import com.example.sinner.letsteacher.kotterknife.bindView
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by win7 on 2017-06-14.
 */

class HomeFragment : BaseFragment(), View.OnClickListener {
    internal var isDebug = true
    private var img_test: ImageView? = null

    internal val rv_list: RecyclerView? by bindView(R.id.rv_list_home)

    internal val layut_root: FrameLayout by bindView(R.id.home_sceneroot)

    private var data_files: MutableList<FileBean>? = null
    lateinit var adapter: FileListAdapter
    //    @BindView(R.id.tv_test_test)
    lateinit var tv_groupname: TextView
    lateinit var tv_groupback: TextView
    internal var tv_actions: TextView? = null
    internal var tv_tip1: TextView? = null
    internal var tv_tip2: TextView? = null
    internal var tv_tip3: TextView? = null
    internal var tv_tip4: ImageView? = null
    internal val fab: FloatingActionButton by bindView(R.id.fb_home_save)

    internal var specialView: View? = null

    internal var currentselect = -1

    private var scene1: Scene? = null
    private var scene2: Scene? = null
    private var isScene = false
    private var transition: Transition? = null
    lateinit var circularReveal: Animator
    lateinit var endcularReveal: Animator

    internal var i = 0

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun getMianLayout(): Int {
        return R.layout.layout_fragment_homepage
    }

    override fun initView() {
        img_test = mainView.findViewById(R.id.img_test) as ImageView
        //        tv_test= (TextView) mainView.findViewById(R.id.home_sceneroot);
    }

    override fun initAction() {
        img_test!!.setOnClickListener {
            //                VectorDrawableCompat drawableCompat;
            //                Drawable drawable = ((AppCompatImageView)view).getDrawable();
            //                if (drawable instanceof Animatable) {
            //                    ((Animatable) drawable).start();
            //                }
            // ViewCompat.animate(img_test).rotationYBy(90).rotationY(360).setDuration(1000).start();
            val animationSet = AnimationSet(true)
            val animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f)
            animation.duration = 2000
            animation.interpolator = LinearInterpolator()
            animation.repeatCount = Animation.INFINITE
            animation.repeatMode = Animation.RESTART
            val animation1 = RotateAnimation(0f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            animation1.duration = 2000
            animation1.interpolator = LinearInterpolator()
            animation1.repeatCount = Animation.INFINITE
            animation1.repeatMode = Animation.RESTART
            animationSet.addAnimation(animation)
            animationSet.addAnimation(animation1)

            animationSet.repeatMode = Animation.RESTART
            animationSet.repeatCount = Animation.INFINITE
            //TranslateAnimation animation=new TranslateAnimation(0,20,0,20);
            img_test!!.startAnimation(animationSet)
        }
    }

    override fun initData() {
        initRv()
        changeLayoutToAdd()
    }

    /**
     * 初始化rv
     */
    private fun initRv() {
        val layoutManager = GridLayoutManager(activity, 4)
        layoutManager.isAutoMeasureEnabled = true
        rv_list!!.layoutManager = layoutManager
        data_files = ArrayList()
        adapter = FileListAdapter(data_files, object : FileListAdapter.OnEventClick() {
            override fun onItemClick(view: View, position: Int) {

                val intent: Intent
                if (position == data_files!!.size) {//添加页面
                    //                    intent=new Intent(activity, AddNewFileMkdirs.class);
                    //                    startActivityForResult(intent,3/*,ac.toBundle()*/);
                    if (specialView == null) specialView = view
                    if (currentselect != -1)
                        layut_root!!.findViewById(R.id.root).visibility = View.VISIBLE
                    if (specialView == null) specialView = view
                    switchScene(specialView, !isScene)
                    // startActivity(new Intent(activity, TestActivity.class));
                    //                    ShapeFragment fragment=new ShapeFragment();
                    //                    fragment.show(getActivity().getSupportFragmentManager(), "searchdialog");
                } else {//打开操作
                    currentselect = position
                    if (isScene && specialView != null) {
                        Handler().postDelayed({ gotoDetail(position, view) }, 500)
                        switchScene(specialView, !isScene)
                    } else {
                        gotoDetail(position, view)
                    }
                }
            }
        })
        rv_list!!.adapter = adapter
        GeFileList()
    }


    /**
     * 转换布局
     */
    private fun changeLayoutToAdd() {
        val sceneRoot = layut_root
        scene1 = Scene.getSceneForLayout(sceneRoot, R.layout.layout_frame_sign, activity)
        scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.layout_frame_nosign, activity)
        TransitionManager.go(scene2)
        //FindAllInfoView();
    }

    /**
     * 查找填充信息布局控件
     */
    private fun FindAllInfoView() {
        //        tv_groupname= (TextView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_name);
        //        tv_grouptime= (TextView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_ctime);
        //        tv_actions= (TextView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_action);
        //        tv_tip1= (TextView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_tip);
        //        tv_tip2= (TextView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_tip1);
        //        tv_tip3= (TextView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_tip2);
        //        tv_tip4=(ImageView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_tip3);
        //
        //
        //        tv_actions.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //
        //            }
        //        });
    }

    private fun ShowOrHide(isshow: Boolean) {
        tv_tip4!!.visibility = if (isshow) View.VISIBLE else View.INVISIBLE
    }

    /**
     * 展示文件夹操作选项
     */
    private fun showActions() {}

    private fun switchScene(view: View?, flag: Boolean) {
        if (transition == null) {
            val slide = Slide()
            slide.duration = 500
            slide.slideEdge = Gravity.LEFT
            val fade = Fade()
            fade.duration = 500
            transition = TransitionSet().addTransition(slide)
        }


        if (flag) {
            initStartAnimation()
            circularReveal.start()
            var x = 0f
            var y = 0f
            val sunwidth = rv_list!!.measuredWidth
            x = sunwidth.toFloat() - view!!.x - view.measuredWidth.toFloat() + 30
            y = (view.height / 2 - 20).toFloat()
            Logs.e("x" + view.x, "y" + view.y)
            Logs.e("sx$sunwidth---x$x", "sy" + y)
            Logs.e("w" + view.measuredWidth, "h" + view.measuredHeight)
            ViewCompat.animate(view).rotation(135f).scaleX(0.5f).scaleY(0.5f).setDuration(700).setInterpolator(AnticipateOvershootInterpolator()).translationX(x).translationY(y).start()
        } else {
            initEndAnimation()
            endcularReveal.start()
            ViewCompat.animate(view).rotation(0f).scaleX(1.0f).scaleY(1.0f).setDuration(700).setInterpolator(AnticipateOvershootInterpolator()).translationX(0f).translationY(0f).start()
        }
        isScene = !isScene
        TransitionManager.go(if (flag) scene1 else scene2, transition)
        if (flag) {
            tv_groupname = scene1!!.sceneRoot.findViewById(R.id.ed_add_gruopname) as TextView
            tv_groupback = scene1!!.sceneRoot.findViewById(R.id.ed_add_gruopback) as TextView
            tv_groupname.setOnClickListener(this)
            tv_groupback.setOnClickListener(this)
        }
    }


    @OnClick(R.id.fb_home_save)
    fun OnClickEvent(view: View) {
        when (view.id) {
            R.id.fb_home_save -> CheckCommit()
        }
    }

    /**
     * 展示
     */
    //    private void showGroupDetail(int position,View view){
    //        FileBean data=data_files.get(position);
    //        tv_groupname.setText(data.getName());
    //        tv_grouptime.setText(data.getCreatedAt());
    //    }
    private fun gotoDetail(position: Int, view: View) {
        val intent: Intent
        intent = Intent(activity, ShowNetPicActivity::class.java)
        intent.putExtra("name", data_files!![position].name)
        intent.putExtra("id", data_files!![position].groupid)
        val ac = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair(view.findViewById(R.id.tv_flieitem_name), "title"))
        startActivityForResult(intent, 2, ac.toBundle())
    }

    private fun initEndAnimation() {
        endcularReveal = ViewAnimationUtils.createCircularReveal(layut_root, 0, 0, 1.5f * Math.max(layut_root!!.width, layut_root!!.height), 0f)
        endcularReveal.duration = 500
        endcularReveal.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                layut_root!!.setBackgroundColor(resources.getColor(R.color.menu_blue))
                AnimatorUtil.scaleHide(fab, 500, object : ViewPropertyAnimatorListener {
                    override fun onAnimationStart(view: View) {

                    }

                    override fun onAnimationEnd(view: View) {
                        fab!!.visibility = View.INVISIBLE
                    }

                    override fun onAnimationCancel(view: View) {

                    }
                })
            }

            override fun onAnimationEnd(animator: Animator) {
                layut_root!!.setBackgroundColor(Color.WHITE)

            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
    }

    private fun initStartAnimation() {
        circularReveal = ViewAnimationUtils.createCircularReveal(layut_root, 0, 0, 0f, 1.5f * Math.max(layut_root!!.width, layut_root!!.height))
        circularReveal.duration = 500
        circularReveal.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                AnimatorUtil.scaleShow(fab, 500, object : ViewPropertyAnimatorListener {
                    override fun onAnimationStart(view: View) {
                        fab!!.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(view: View) {

                    }

                    override fun onAnimationCancel(view: View) {

                    }
                })
                //layut_root.setBackgroundColor(getResources().getColor(R.color.menu_blue));
                //                tv_test= (TextView) layut_root.findViewById(R.id.tv_test_test);
                //                if(tv_test!=null)
                //                    tv_test.setText("text"+i);
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            2 -> {
            }
        }//                data_files.clear();
        //                GeFileList();
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun GeFileList() {

        showProgressDialog()
        val map = HashMap<String, String>()
        map.put("userid", "")
        BmobUtil.getInstance().SearchData(FileBean::class.java, null, object : BmobQueryListener<FileBean>() {

            override fun OnSuccess(`object`: FileBean) {

            }

            override fun OnSuccess(objects: List<FileBean>) {
                Logs.e("objects", "---" + objects.size)
                for (i in objects.indices) {
                    if (objects[i] != null) {
                        data_files!!.add(objects[i])
                    }
                }
                hideProgressDialog()
                //                data_files.add(new FileBean());
                adapter.notifyItemRangeChanged(0, data_files!!.size)
            }

            override fun OnFail(code: String, error: String) {
                hideProgressDialog()
                Logs.e("查询出错--" + code, error)
            }
        })
    }

    /**
     * 检查是否超过文件夹最大限制 8个
     */
    private fun CheckCount() {
        val temp = FileBean()
        //        temp.setUserid(LoginUserEntity.userid);
        val limit = HashMap<String, Any>()
        limit.put("userid", LoginUserEntity.userid)
        showProgressDialog()
        BmobUtil.getInstance().CountData(temp, limit, object : CountListener() {
            override fun done(integer: Int?, e: BmobException?) {
                if (e == null) {
                    if (integer ?: 8 < 8) {
                        addGroup()
                    } else {
                        hideProgressDialog()
                        SuperToastUtil.getInstance(activity).showToast("抱歉!至多只能创建8个目录")
                    }
                } else {
                    hideProgressDialog()
                    Logs.e("" + e.errorCode, "" + e.message)
                    SuperToastUtil.getInstance(activity).showToast("抱歉,提交出现了问题！" + e.errorCode)
                }
            }
        })
    }


    private fun addGroup() {
        val bean = FileBean()
        Logs.e("---id", "" + bean.objectId)
        bean.name = tv_groupname.text.toString()
        bean.remark = tv_groupback.text.toString()
        bean.colorids = AndroidUtils.getRandomColor()
        bean.groupid = "" + System.currentTimeMillis()
        bean.userid = LoginUserEntity.userid
        //        bean.setGroupid(bean.getObjectId());
        bean.isDelete = false
        showProgressDialog()
        BmobUtil.getInstance().addData(bean, object : BmobAddOrUpdateListener() {
            override fun OnSuccess(backstr: String) {
                hideProgressDialog()
                SuperToastUtil.getInstance(activity).showToast("保存成功！")
                if (isScene)
                    switchScene(specialView, false)
                Handler().postDelayed({ Refresh() }, 400)
            }

            override fun OnFail(errormsg: String) {
                hideProgressDialog()
                SuperToastUtil.getInstance(activity).showToast("提交失败!" + errormsg)
            }
        })
    }

    /**
     * 更新接口
     */
    private fun Refresh() {
        specialView = null
        initRv()
    }


    override fun clear() {

    }

    override fun onClick(view: View) {
        val dialog = TextDialog(activity, R.style.DialogStyle)
        dialog.setText((view as TextView).text.toString())
        dialog.setListener { str -> view.text = str }
        dialog.show()
    }

    private fun CheckCommit() {
        val name = tv_groupname.text.toString()
        val remark = tv_groupback.text.toString()
        Logs.e("name", "" + name)
        Logs.e("back", "" + remark)
        if (StringTools.getNotNullStr(name).length != 0/*&&StringTools.getNotNullStr(remark).length()!=0*/) {
            CheckCount()
        } else {

        }
    }
}
