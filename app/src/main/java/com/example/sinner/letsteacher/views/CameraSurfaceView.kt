package com.example.sinner.letsteacher.views

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.jar.Attributes

/**
 * Created by sinner on 2017-08-26.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
class CameraSurfaceView  :SurfaceView, SurfaceHolder.Callback {


    constructor(context:Context, attrs: AttributeSet, defStyleAttr:Int=0) : super(context,attrs,defStyleAttr){ init() }
    constructor(context: Context) :super(context){ init()}
    private val  TAG="CameraSurfaceView"
    private var mContext:Context?=null;
  //  private  var mholder :SurfaceHolder?=null

    fun init(){
        holder.addCallback(this)
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)//
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
    }

}