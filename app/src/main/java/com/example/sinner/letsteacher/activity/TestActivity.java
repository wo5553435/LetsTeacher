package com.example.sinner.letsteacher.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;

/**
 * Created by sinner on 2017-09-11.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        TextView textView=new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        textView.setText("点击测试");
        setContentView(textView);
        initBmob();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFiles();
            }
        });
    }

    private void initBmob() {
        BmobConfig config =new  BmobConfig.Builder(this)
                //        //设置appkey
                .setApplicationId("0356f50f31a983aeae8156765c7a3284")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
    }


    private void deleteFiles() {
        String file1="http://bmob-cdn-13614.b0.upaiyun.com/2017/09/05/1776d9be515141248f5a6da94a9c0abe.jpg";
        String file2="http://bmob-cdn-13614.b0.upaiyun.com/2017/09/05/818b58dedb554958b38cc60cb620a891.jpg";
        String[] urls={file1,file2};

        BmobFile.deleteBatch(urls, new DeleteBatchListener() {
            @Override
            public void done(String[] failurls, BmobException e) {

                if(e==null){
                    Log.e("全部删除成功","");
                   // listener.OnSuccess("全部删除成功");
                }else{
                    e.printStackTrace();
                    if(failurls!=null){
                        Log.e("删除失败",""+e.toString());
                      //  listener.OnFail("删除失败",failurls.length+","+e.toString());
                    }else{
                        Log.e("全部文件删除失败",""+e.toString());
                      //  listener.OnFail("全部文件删除失败:",e.getErrorCode()+","+e.toString());
                    }
                }
            }
        });
    }
}
