package com.example.sinner.letsteacher.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sinner.letsteacher.MyApplication;
import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.entity.ImageVo;
import com.example.sinner.letsteacher.utils.AndroidUtils;
import com.example.sinner.letsteacher.utils.Logs;
import com.example.sinner.letsteacher.utils.SuperToastUtil;
import com.example.sinner.letsteacher.utils.bmob.BmobUtil;
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobAddOrUpdateListener;
import com.example.sinner.letsteacher.utils.share.ShareUtil;
import com.pigcms.library.android.okhttp.HttpUtils;
import com.pigcms.library.android.okhttp.callback.filecallback.MyFileRequestCallback;
import com.pigcms.library.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by sinner on 2017-08-10.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class ImgDetailActivity extends BasicActivity {
    final  String  test_url="http://bmob-cdn-13410.b0.upaiyun.com/2017/08/10/1bfad77c48b940f589767c4228a5f1db.jpg";

    final String folderurl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tuji";//下载文件地址
    @BindView(R.id.img_test)
    ImageView img_test;
    @BindView(R.id.tv_test)
    TextView tv_test;
    @BindView(R.id.banner2)
    Banner banner;
    @BindView(R.id.layout_img_title)
    RelativeLayout layout_title;
    @BindView(R.id.layout_img_action)
    RelativeLayout layout_bottom;

    @BindView(R.id.icon_upload)
    ImageView icon_download;
    private List<String> images=new ArrayList<>();
    private int position=0;
    int height;
    private boolean DownLoadMode=false;
    @BindDrawable(R.drawable.ic_cloud_upload_black_24dp)
    Drawable drawable_upload;
    @BindDrawable(R.drawable.ic_action_download)
    Drawable drawable_download;


    @Override
    protected int getContentLayout() {
        return R.layout.layout_activity_detail;
    }

    @Override
    protected void initGui() {
        initBottom();
    }

    /**
     * 根据传过来的字段显示不同功能
     */
    private void initBottom() {
        if(getIntent().hasExtra("download")){
            DownLoadMode=true;
            icon_download.setImageDrawable(drawable_download);
        }
    }

    @Override
    protected void initData() {
        height=AndroidUtils.Companion.dip2px(activity,50);
        if(getIntent().hasExtra("images")){
            images=getIntent().getStringArrayListExtra("images");
            position=getIntent().getIntExtra("point",0);
            initbanner();
        }
    }

    private void initbanner() {
        banner.isAutoPlay(false);
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.setImages(images);
        //设置指示器居中
        banner.SetBannerSelect(position);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {
                Logs.e("positon",""+position);
                ShowOrHidelayout(layout_bottom.getVisibility()==View.INVISIBLE);
            }
        });
    }

    @Override
    protected void initAction() {
        icon_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!DownLoadMode)
                uploadImg(images.get(banner.getCurrentItem()-1));
                else{
                    DownloadImg(images.get(banner.getCurrentItem()-1));
                }
            }
        });
        tv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Glide.with(activity).load(test_url).crossFade()
//                        .into(img_test);
                shareTo(images.get(banner.getCurrentItem()-1));
            }
        });
    }
    private void shareTo(String s){
        showProgressDialog();
        String [] ss=s.split("\\/");
        HttpUtils.getInstance().downLoadFile(s, folderurl,ss[ss.length-1], new MyFileRequestCallback<File>() {
            @Override
            public void OnSuccess(final File file) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        new ShareUtil(activity).shareMsg("ceshi1","ceshi2",file.getAbsolutePath());
                        //SuperToastUtil.getInstance(activity).showToast("下载成功!保存到"+file.getAbsolutePath());
                    }
                });

            }

            @Override
            public void onPogress(long total, long current) {

            }

            @Override
            public void OnFail(String arg0, final String arg1) {

                Logs.e(arg0,arg1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        SuperToastUtil.getInstance(activity).showToast("下载出错!:"+arg1);
                    }
                });

            }
        });
    }

    private void DownloadImg(String s) {
        showProgressDialog();
        String [] ss=s.split("\\/");
        HttpUtils.getInstance().downLoadFile(s, folderurl,ss[ss.length-1], new MyFileRequestCallback<File>() {
            @Override
            public void OnSuccess(final File file) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        SuperToastUtil.getInstance(activity).showToast("下载成功!保存到"+file.getAbsolutePath());
                    }
                });

            }

            @Override
            public void onPogress(long total, long current) {

            }

            @Override
            public void OnFail(String arg0, final String arg1) {

                Logs.e(arg0,arg1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        SuperToastUtil.getInstance(activity).showToast("下载出错!:"+arg1);
                    }
                });

            }
        });
    }

    boolean isAnimation=false;

    public void ShowOrHidelayout(final boolean isshow){
        if(isAnimation) return;
        ValueAnimator valueAnimator=isshow?ValueAnimator.ofFloat(0,1):ValueAnimator.ofFloat(1,0);
        valueAnimator.setInterpolator(new FastOutSlowInInterpolator());
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                SetLayoutHeight((Float) valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimation=true;
                if(isshow){
                    layout_bottom.setVisibility(View.VISIBLE);
                    layout_title.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimation=false;
                if(!isshow){
                layout_bottom.setVisibility(View.INVISIBLE);
                layout_title.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.start();
    }

    public  void SetLayoutHeight(float percent){
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) layout_bottom.getLayoutParams();
        RelativeLayout.LayoutParams params1=(RelativeLayout.LayoutParams) layout_title.getLayoutParams();
        params.height= (int) (height*percent);
        params1.height= (int) (height*percent);
        layout_title.setLayoutParams(params1);
        layout_bottom.setLayoutParams(params);
    }


    private void uploadImg(String url){
        Logs.e("url",url);
        showProgressDialog();
        File image=new File(url);
        Log.e("image is exists?"+image.exists(),"size"+image.length());
        final BmobFile file=new BmobFile(image);
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                hideProgressDialog();
               if(e==null){

                    Logs.e("file url",file.getFileUrl());
                   updateUserImg(file.getFileUrl());

               }else{
                   Logs.e("fail "+e.getErrorCode(),"msg:"+e.getMessage());
                   SuperToastUtil.getInstance(activity).showToast("上传失败"+e.getMessage());
               }
            }
        });
    }

    /**
     *
     */
    private void updateUserImg(String fileurl){
       if(((MyApplication)getApplication()).getUserVo()!=null){
           String userid=((MyApplication)getApplication()).getUserVo().getId();
           ImageVo imageVo=new ImageVo(userid,fileurl);
           BmobUtil.getInstance().addData(imageVo, new BmobAddOrUpdateListener() {
               @Override
               public void OnSuccess(String backstr) {
                   SuperToastUtil.getInstance(activity).showToast("上传成功");
               }

               @Override
               public void OnFail(String errormsg) {
                   SuperToastUtil.getInstance(activity).showToast("保存地址失败"+errormsg);
               }
           });
       }
    }
}
