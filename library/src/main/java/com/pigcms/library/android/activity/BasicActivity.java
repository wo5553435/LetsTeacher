package com.pigcms.library.android.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;


//import butterknife.ButterKnife;
//import butterknife.Unbinder;

/**
 * Created by win7 on 2016/8/29.
 */

public abstract  class BasicActivity extends AppCompatActivity {
    /*


    protected Activity activity;
    private String appname="";
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedactivityState) {
        if (savedactivityState != null) {//长时间不操作，不保存fragment其中内容
            savedactivityState.putParcelable("android:support:fragments",null);
        }
        super.onCreate(savedactivityState);
        // 取消标题栏
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getContentLayout() != 0) {
            setContentView(getContentLayout());
        }
        activity = this;
        appname=getResources().getString(R.string.app_name);
        unbinder=ButterKnife.bind(this);
        initGui();
        initAction();
        initData();

    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Constant.isPosClient){
            boolean safe = AntiHijackingUtil.checkActivity(this);
            if (!safe) {
                ToastTools.showLong(this, R.string.HijackingTip);
                showNotification();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//这句也是为了调用xgpush的通知
    }

    public void showNotification() {

        NotificationManager notificationManager = (
                NotificationManager)getSystemService(
                android.content.Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(appname+"正在后台运行");
        builder.setContentText("必须在"+appname+"APP中发起的交易才可正常记录交易流水。点击返回");
        builder.setSmallIcon(R.drawable.icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon));
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText("必须在"+appname+"APP中发起的交易才可正常记录交易流水。点击返回");
        style.setBigContentTitle("请返回"+appname+"完成收银工作");
        //SummaryText没什么用 可以不设置
        style.setSummaryText("请您注意您的付款方式!");
        builder.setStyle(style);
        builder.setOngoing(true);
        builder.setAutoCancel(true);
        Intent intent = new Intent(this,activity.getClass());
        PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,0);
        builder.setContentIntent(pIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        Notification notification = builder.build();
        notificationManager.notify(0,notification);

    }



    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constant.isPosClient){
            NotificationManager manger = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
            manger.cancel(0);
        }
    }

    *//**
     * 设置布局文件
     *//*
    protected abstract int getContentLayout();

    *//**
     * 初始化UI
     *
     *//*
    protected abstract void initGui();

    *//**
     * 初始化数据
     *//*
    protected abstract void initData();

    *//**
     * 初始化事件
     *//*
    protected abstract void initAction();

    *//**
     * 根据资源id获取值
     *//*
    protected String getResString(int resId) {
        return activity.getResources().getString(resId);
    }

    private long lastClickTime;

    *//**
     * 判断事件出发时间间隔是否超过预定值
     *//*
    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    public void startActivity(Intent intent) {
        // 防止连续点击
        if (isFastDoubleClick()) {
            Logs.i("TAG", "startActivity() 重复调用");
            return;
        }
        super.startActivity(intent);
    }

    public void onEvent(Object object) {
    }



    *//**
     * 开启等待层
     *//*
    public void showProgressDialog() {
        if (dialog == null) {
            dialog = new WaitingDialog(this, R.style.WaitingDialogStyle);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
        }
        dialog.show();
    }

    *//**
     * 隐藏等待层
     *//*
    public void hideProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    *//**
     * 判断等待层是否开启状态
     *
     * @return 是否
     *//*
    public boolean isShowDialog() {
        if (dialog != null)
            if (dialog.isShowing())
                return true;
        return false;
    }
*/
}
