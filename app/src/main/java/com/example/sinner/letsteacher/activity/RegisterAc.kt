package com.example.sinner.letsteacher.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.entity.ApplyVo
import com.example.sinner.letsteacher.utils.SuperToastUtil
import com.example.sinner.letsteacher.utils.bmob.BmobUtil
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobAddOrUpdateListener
import kotlinx.android.synthetic.main.activity_register2.*;
import kotlinx.android.synthetic.main.layout_title_blue.*

class RegisterAc : BasicActivity() {
    override fun getContentLayout()=R.layout.activity_register2

    override fun initGui() {
        ed_regis_username.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(ed_regis_username.text.toString().length==0){
                    btn_regis_commit.isEnabled=false
                    btn_regis_commit.setBackgroundColor(resources.getColor(R.color.menu_enable))
                }else{
                    btn_regis_commit.isEnabled=true
                    btn_regis_commit.setBackgroundColor(resources.getColor(R.color.menu_blue))
                }
            }

        })

    }

    override fun initData() {
        title_text.text="提交申请"
    }

    /**
     * 申请表查询 插入数据
     */
    fun  applyfor(){
        showProgressDialog()
        val temp=ApplyVo(ed_regis_username.text.toString());
        var limit= mapOf<String,String>("username" to ed_regis_username.text.toString())
        BmobUtil.getInstance().addDataCheck(temp,"ApplyVo",limit,object :BmobAddOrUpdateListener(){
            override fun OnSuccess(backstr: String?) {
                hideProgressDialog()
                SuperToastUtil.getInstance(activity).showToast("提交成功！")
            }

            override fun OnFail(errormsg: String?) {
                hideProgressDialog()
                if(errormsg?.startsWith("该")!!){
                    SuperToastUtil.getInstance(activity).showToast("已经完成提交的账号不用重复申请~")
                }else
                SuperToastUtil.getInstance(activity).showToast(errormsg)
            }
        })
    }

    override fun initAction() {
        btn_regis_commit.setOnClickListener{
            applyfor()
        }
    }

}
