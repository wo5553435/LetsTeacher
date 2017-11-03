package com.example.sinner.letsteacher.activity.camera

import android.media.MediaRecorder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.activity.BasicActivity
import com.example.sinner.letsteacher.kotterknife.bindView
import com.pigcms.library.utils.CommonUtil
import com.pigcms.library.utils.SuperToastUtil
import  kotlinx.android.synthetic.main.activity_camera.*;
import java.io.File
import com.example.sinner.letsteacher.interfaces.behavior.ListenerAnimatorEndBuild.isFinish



class CameraActivity : BasicActivity() {

    private val TAG = "MainActivity"
    private var isprepare = true;//是否可以录制


    override fun getContentLayout() = R.layout.activity_camera

    override fun initGui() {

    }

    override fun initAction() {
        btnStartStop.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> recordview.record(object : MovieRecorderView.OnRecordFinishListener {
                    override fun onRecordFinish() {
                        finishRecord()
                    }
                })
                MotionEvent.ACTION_UP ->{
                    if(recordview.timeCount>10) finishRecord()
                    else{//不超过1秒的删除
                        if(recordview.getmRecordFile()!=null) recordview.getmRecordFile().delete()
                        recordview.stop()
                        SuperToastUtil.getInstance(activity).showToast("录制时间太短")
                    }
                }
            }
            true
        }

    }

    fun finishRecord() {

    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        isprepare=true
    }

     override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
         isprepare = false
        recordview.stop()
    }

    private fun initRecord() {

    }

    /**
     * 初始化surface
     */
    private fun start() {
    }

    private fun getCurrenttimeFileurl(url: String) = url + "/" + System.currentTimeMillis() + ".3gp"


}
