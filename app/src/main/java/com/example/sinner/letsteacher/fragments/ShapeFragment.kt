package com.example.sinner.letsteacher.fragments

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import android.widget.TextView
import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.utils.AndroidUtils
import com.example.sinner.letsteacher.utils.AnimatorUtil
import com.example.sinner.letsteacher.utils.Logs

/**
 * Created by sinner on 2017-09-09.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
class ShapeFragment : DialogFragment(){

    var itemView: View? = null
    var text:TextView ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logs.e("---","onCreate")

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logs.e("---","onCreateView")
        itemView = inflater?.inflate(R.layout.layout_shapedialog, container)
        initView()
        initData()
        return itemView
    }


    override fun onStart() {
        Logs.e("---","onstart")
        initDialog()
        AnimatorUtil.scaleShow(text,null)
//        itemView.startAnimation()
        super.onStart()
    }

    private fun initDialog() {
        val window = dialog.window
        val metrics = resources.displayMetrics
        val width = (metrics.widthPixels * 0.8).toInt() //DialogSearch的宽
        window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        window.setWindowAnimations(R.style.DialogEmptyAnimation)//取消默认过渡动画
    }

    fun  initView(){
        text=itemView?.findViewById(R.id.text) as TextView
    }
    fun  initData(){
    }
}