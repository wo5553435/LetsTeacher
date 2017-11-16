package com.example.sinner.letsteacher.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment

import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.utils.SuperToastUtil
import com.tencent.tinker.lib.tinker.TinkerInstaller
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals
import kotlinx.android.synthetic.main.activity_tinker.*
import java.io.File

class TinkerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tinker)
        initData()
    }

    private fun initData() {
        btn_addpacth.setOnClickListener{
         var patchurl=   Environment.getExternalStorageDirectory().getAbsolutePath() + "/apks/patch.apk"
            if(File(patchurl).exists()){
                TinkerInstaller.onReceiveUpgradePatch(applicationContext,patchurl)
            }else SuperToastUtil.getInstance(this@TinkerActivity).showToast("未找到打包文件")
        }

        btn_test.setOnClickListener{
            ShareTinkerInternals.killAllOtherProcess(getApplicationContext());
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
