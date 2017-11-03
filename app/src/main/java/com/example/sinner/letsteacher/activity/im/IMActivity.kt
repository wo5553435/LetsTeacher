package com.example.sinner.letsteacher.activity.im

import android.widget.TextView
import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.activity.BasicActivity
import com.example.sinner.letsteacher.kotterknife.bindView

/**
 * Created by sinner on 2017-10-09.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
class IMActivity :BasicActivity() {
    val text:TextView by bindView(R.id.tv_action1)
    override fun getContentLayout()=0

    override fun initGui() {
    }

    override fun initData() {
    }

    override fun initAction() {
    }

}