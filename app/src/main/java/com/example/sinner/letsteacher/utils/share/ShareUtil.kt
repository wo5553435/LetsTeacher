package com.example.sinner.letsteacher.utils.share

import android.content.Context
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.utils.SuperToastUtil
import java.io.File


/**
 * Created by sinner on 2017-09-26.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
class ShareUtil (var context: Context) {

        var testitem= ShareItem("QQ", R.drawable.icon,
    "com.tencent.mobileqq.activity.JumpActivity","com.tencent.mobileqq")

     fun shareMsg( msgTitle: String, msgText: String,
                         imgPath: String?) {
        var  share=testitem
        if (!share.packageName.isEmpty() && !isAvilible(context, share.packageName)) {
            SuperToastUtil.getInstance(context).showToast("未安装" + share.title)
            return
        }

        val intent = Intent("android.intent.action.SEND")
        if (imgPath == null || imgPath == "") {
            intent.setType("text/plain")
        } else {
            val f = File(imgPath)
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/png")
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f))
            }
        }

        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle)
        intent.putExtra(Intent.EXTRA_TEXT, msgText)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (!share.packageName.isEmpty()) {
            intent.setComponent(ComponentName(share.packageName, share.activityName))
            context.startActivity(intent)
        } else {
            context.startActivity(Intent.createChooser(intent, msgTitle))
        }
    }

    fun queryPackage(): List<ComponentName> {
        val cns = ArrayList<ComponentName>()
        val i = Intent("android.intent.action.SEND")
        i.setType("image/*")
        val resolveInfo = context.getPackageManager().queryIntentActivities(i, 0)
        for (info in resolveInfo) {
            val ac = info.activityInfo
            val cn = ComponentName(ac.packageName, ac.name)
            cns.add(cn)
        }
        return cns
    }


    fun isAvilible(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager

        val pinfo = packageManager.getInstalledPackages(0)
        for (i in pinfo.indices) {
            if ((pinfo[i] as PackageInfo).packageName.equals(packageName,true))
                return true
        }
        return false
    }


     inner class ShareItem(internal var title: String, internal var logo: Int, internal var activityName: String, internal var packageName: String)
}