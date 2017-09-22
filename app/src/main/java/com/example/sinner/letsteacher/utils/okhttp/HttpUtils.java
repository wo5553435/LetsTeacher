package com.example.sinner.letsteacher.utils.okhttp;

import android.app.Activity;
import android.util.Log;


import com.example.sinner.letsteacher.utils.Logs;
import com.example.sinner.letsteacher.utils.okhttp.callback.ResponseCallback;
import com.example.sinner.letsteacher.utils.okhttp.callback.filecallback.MyFileRequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by win7 on 2016/9/13.
 */

public class HttpUtils {

    private static HttpUtils instance;

    private OkHttpClient mOkHttpClient;

    private String filename="";

    private HttpUtils(){
        if(mOkHttpClient==null)
            mOkHttpClient=new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).
                    readTimeout(15, TimeUnit.SECONDS).build();

    }

    public  static HttpUtils getInstance(){
        if(instance==null)
            instance=new HttpUtils();
        return instance;
    }


//    /**
//     * 新版post带参请求,这里需要注意，boolean的参数是指你需不需要直接返回data中的数据，当为true时返回的不是外包msg和error参数的vo类
//     * @param activity
//     * @param url
//     * @param params
//     * @param callback
//     * @param objectflag 是否指定结果为object
//     */
//    public void Post(final Activity activity, String url, Map<String, String> params, final ResponseCallback callback, final boolean objectflag){
//        FormBody.Builder formBodyBuilder = new FormBody.Builder();
//        if (params != null && params.size() > 0) {
//            Set<Map.Entry<String, String>> entrySet = params.entrySet();
//            for (Map.Entry<String, String> entry : entrySet) {
//                formBodyBuilder.add(entry.getKey(), entry.getValue());
//            }
//        }
//        Request request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
//        Call call = mOkHttpClient.newCall(request);
//
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, final IOException e) {
//                if(!activity.isFinishing())
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Logs.e("onFailure","in okhttp");
//                            if(e instanceof SocketTimeoutException){
//                                callback.OnFail("-888","网络连接超时，请稍后重试");
//                            }else{
//                                callback.OnFail("-900",e.getMessage());
//                            }
//                        }
//                    });
//
//
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                final String str = response.body().string();
//                if(!activity.isFinishing())
//
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            Logs.i("onResponse", str);
//                            if(response.isSuccessful())
//                                if(objectflag){
//                                }else
//                                callback.OnSuccess(str);
//                            else
//                                callback.OnFail(""+response.code(),response.message());
//                        }
//                    });
//
//            }
//
//        });
//       /* try {
//            return call.execute().body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;*/
//    }

    /**
     * 兼容老版本的post请求，返回为字符串类型
     * @param activity
     * @param url
     * @param params
     * @param callback
     */
    public void Post(final Activity activity, String url, Map<String, String> params,final ResponseCallback callback){
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if(!activity.isFinishing())
                activity.runOnUiThread(new Runnable() {//恕我愚钝，本在旧框架的问题，在返回后有些耗时操作（gson）也放到了主线程，也不敢加上Rxjava，智只能祈祷不要太耗时
                    @Override
                    public void run() {
                        Logs.e("onFailure","in okhttp");
                        if(e instanceof SocketTimeoutException){
                            callback.OnFail("-888","网络连接超时，请稍后重试");
                        }else if(e instanceof ConnectException){
                            callback.OnFail("-800","网络连接错误，请稍后重试");
                        }else if(e instanceof UnknownHostException){
                            callback.OnFail("-800","网络连接错误，请稍后重试");
                        }else{
                            callback.OnFail("-900",e.getMessage());
                        }
                    }
                });


            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String str = response.body().string();
                if(!activity.isFinishing())

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Logs.i("onResponse", str);
                        if(response.isSuccessful())
                            callback.OnSuccess(str);
                        else
                            callback.OnFail(""+response.code(),response.message());
                    }
                });

            }

        });
    }


    /**
     * 兼容老版本的post请求，返回为字符串类型
     * @param activity
     * @param url
     * @param params
     * @param callback
     * @param isEncrypt 是否加密
     */
    public void Post(final Activity activity, String url, Map<String, String> params,boolean isEncrypt,final ResponseCallback callback){
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        JSONObject jsonObject = new JSONObject();



            if(isEncrypt){
            if (params != null && params.size() > 0) {
                Set<Map.Entry<String, String>> entrySet = params.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    try {
                        jsonObject.put(entry.getKey(),entry.getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //formBodyBuilder.add(entry.getKey(), entry.getValue());
                //formBodyBuilder.add("pdvs", EncryptUtil.getEncryptValue(jsonObject.toString()));
                //formBodyBuilder.add("sign",EncryptUtil.getEncryptSign(jsonObject.toString()));

            }
        }

    else{
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
    }

        Request request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if(!activity.isFinishing())
                    activity.runOnUiThread(new Runnable() {//恕我愚钝，本在旧框架的问题，在返回后有些耗时操作（gson）也放到了主线程，也不敢加上Rxjava，智只能祈祷不要太耗时
                        @Override
                        public void run() {
                            Logs.e("onFailure","in okhttp");
                            if(e instanceof SocketTimeoutException){
                                callback.OnFail("-888","网络连接超时，请稍后重试");
                            }else if(e instanceof ConnectException){
                                callback.OnFail("-800","网络连接错误，请稍后重试");
                            }else{
                                callback.OnFail("-900",e.getMessage());
                            }
                        }
                    });


            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String str = response.body().string();
                if(!activity.isFinishing())

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Logs.i("onResponse", str);
                            if(response.isSuccessful())
                                callback.OnSuccess(str);
                            else
                                callback.OnFail(""+response.code(),response.message());
                        }
                    });

            }

        });

    }

    /**
     * 说实话 我懒的写同步get请求
     */
    public void Get(){

    }


    //测试apk http://dl.wandoujia.com/files/jupiter/latest/wandoujia-web_seo_baidu_homepage.apk
    /**
     * 无参下载文件
     * @param fileUrl 文件url
     * @param destFileDir 存储目标目录
     */
    public <T> void downLoadFile(String fileUrl, final String destFileDir, String filename ,final MyFileRequestCallback<T> callBack) {
        //final String fileName = MD5.encode(fileUrl);
        final File file1 = new File(destFileDir);
        if (!file1.exists()) {
            file1.mkdir();
        }

          final  File file =new File(file1.getAbsolutePath()+"/"+filename);
        this.filename=filename;
            if(file.exists()){
                //file.delete();
                callBack.OnSuccess(file);
                return;
            }
        final Request request = new Request.Builder().url(fileUrl).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logs.e("onFailure", e.toString());

                callBack.OnFail("onFailure","下载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    long total_cut=total/50;
                    long currentpercent=0;

                    Log.e("request", "total------>" + total);
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                       // Log.e("onResponse", "current------>" + current);
                        if(current>currentpercent){//当有进度更新时才触发回调
                            currentpercent=currentpercent+total_cut;
                            callBack.onPogress(total, current);
                        }
                    }
                    fos.flush();
                    callBack.OnSuccess(file);
                } catch (IOException e) {
                    Log.e("IOException", e.toString());
                    callBack.OnFail("下载失败", "IOException");
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        Log.e("exception", e.toString());
                    }
                }
            }
        });
    }
}
