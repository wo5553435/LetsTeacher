package com.example.sinner.letsteacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.entity.UserVo;
import com.example.sinner.letsteacher.utils.Logs;
import com.example.sinner.letsteacher.utils.StringTools;
import com.example.sinner.letsteacher.utils.SuperToastUtil;
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobAddOrUpdateListener;
import com.example.sinner.letsteacher.utils.bmob.BmobUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BasicActivity {
    @BindView(R.id.title_lin_back)
    LinearLayout titleLinBack;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_repassword)
    EditText edRepassword;
    @BindView(R.id.btn_register_commit)
    Button btnRegisterCommit;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initGui() {
        titleText.setText("注册");

    }

    @Override
    protected void initData() {
        //第一：默认初始化
        //Bmob.initialize(this, "5069096f8313d0c157878a756cf32776");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
//        BmobConfig config =new BmobConfig.Builder(activity)
//        //设置appkey
//        .setApplicationId("5069096f8313d0c157878a756cf32776")
//        //请求超时时间（单位为秒）：默认15s
//        .setConnectTimeout(30)
//        //文件分片上传时每片的大小（单位字节），默认512*1024
//        .setUploadBlockSize(1024*1024)
//        //文件的过期时间(单位为秒)：默认1800s
//        .setFileExpiration(2500)
//        .build();
//        Bmob.initialize(config);
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: add setContentView(...) invocation
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_register_commit)
    public void Register(){
        if(checkDate()){
            UserVo user=new UserVo();
            user.setUsername(edUsername.getText().toString());
            user.setPassword(edPassword.getText().toString());
            user.setPhone(edPhone.getText().toString());

            HashMap<String,Object> params=new HashMap<>();
            params.put("username",user.getUsername());
            showProgressDialog();
            BmobUtil.getInstance().addDataCheck(user, "UserVo", params, new BmobAddOrUpdateListener() {
                @Override
                public void OnSuccess(String backstr) {
                    Logs.e("OnSuccess",""+backstr);
                    SuperToastUtil.getInstance(activity).showToast("注册成功");
                    hideProgressDialog();
                       mHandler.sendEmptyMessageDelayed(0,1000);
                }

                @Override
                public void OnFail(String errormsg) {
                    Logs.e("OnFail",""+errormsg);
                    hideProgressDialog();
                    SuperToastUtil.getInstance(activity).showToast(errormsg);
                }

            });

//            user.save(new SaveListener<String>() {
//                @Override
//                public void done(String s, BmobException e) {
//                    if(e==null){
//                        SuperToastUtil.getInstance(activity).showToast("注册成功");
//                        mHandler.sendEmptyMessageDelayed(0,500);
//                    }else{
//                        SuperToastUtil.getInstance(activity).showToast("出了点情况.."+e.getMessage());
//                    }
//                }
//            });
        }
        //查找Person表里面id为6b6c11c537的数据
//        BmobQuery<UserVo> bmobQuery = new BmobQuery<UserVo>();
//        bmobQuery.getObject("6b6c11c537", new QueryListener<UserVo>() {
//            @Override
//            public void done(UserVo object,BmobException e) {
//                if(e==null){
//                    SuperToastUtil.getInstance(activity).showToast("查询成功");
//                }else{
//                    SuperToastUtil.getInstance(activity).showToast("查询失败：" + e.getMessage());
//                }
//            }
//        });

    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 0:
                    Intent  intent=getIntent();
                    intent.putExtra("isover","true");
                    intent.putExtra("username",edUsername.getText().toString());
                    intent.putExtra("password",edPassword.getText().toString());
                    setResult(2,intent);
                    finish();
                    break;
                case 1:
                    break;
                case 2:
                    break;


            }
            super.handleMessage(msg);
        }
    };

    private boolean checkDate(){
        String username=edUsername.getText().toString();
        String pass=edPassword.getText().toString();
        String repass=edRepassword.getText().toString();
        String phone=edPhone.getText().toString();
        if(StringTools.isEmpty(username)) {SuperToastUtil.getInstance(activity).showToast(1,"用户名不能为空哦");return false;}
        if(StringTools.isEmpty(pass)){SuperToastUtil.getInstance(activity).showToast(1,"密码要设置哦");
        return false;
        }
        if(StringTools.isEmpty(repass)){
            SuperToastUtil.getInstance(activity).showToast(1,"忘记确认密码了么？");
            return false;
        }

        if(!repass.equals(pass)){
            SuperToastUtil.getInstance(activity).showToast(1,"这么快就忘记密码了？");
            return false;
        }


        if(!StringTools.isPhoneNumber(phone)){
            SuperToastUtil.getInstance(activity).showToast(1,"这个手机号码挺奇怪的~");
            return false;
        }
        return true;
    }
}
