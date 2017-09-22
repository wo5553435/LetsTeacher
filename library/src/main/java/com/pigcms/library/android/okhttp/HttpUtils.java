package com.pigcms.library.android.okhttp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.pigcms.library.android.okhttp.callback.PollingCallback;
import com.pigcms.library.android.okhttp.callback.ProgressCallBack;
import com.pigcms.library.android.okhttp.callback.filecallback.MyFileRequestCallback;
import com.pigcms.library.android.okhttp.callback.ResponseCallback;
import com.pigcms.library.android.okhttp.https.HttpsManager;
import com.pigcms.library.android.okhttp.https.MyTrustManager;
import com.pigcms.library.android.okhttp.https.SSLParams;
import com.pigcms.library.android.okhttp.https.UnSafeTrustManager;
import com.pigcms.library.utils.EncryptUtil;
import com.pigcms.library.utils.FileUtil;
import com.pigcms.library.utils.Logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

import static com.pigcms.library.android.okhttp.https.HttpsManager.chooseTrustManager;

/**
 * Created by win7 on 2016/9/13.
 */

public class HttpUtils {

    private static HttpUtils instance;

    private OkHttpClient mOkHttpClient;


    private static Map<String, String> mParamsmap;

    private InputStream is_certificates[] = null;

    private String filename = "";

    private HttpUtils() {
        if (mOkHttpClient == null) {
            SSLParams sslParams = getSslSocketFactory(is_certificates, null, null);//  信任指定证书
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .hostnameVerifier(new HostnameVerifier()//
                    {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;//信任所有域名
                        }
                    })
                    .sslSocketFactory(sslParams.sSLSocketFactory)
                    .build();
        }
    }


    public void getInputStream(Context context) {
    }

    public static HttpUtils getInstance() {
        if (instance == null)
            instance = new HttpUtils();
        return instance;
    }


    /**
     * 马德让加的接口 全部要加lgcode（曾经出现没删除问题）
     *
     * @param Paramsmap
     */
    public static void SetParamsMap(Map<String, String> Paramsmap) {
        mParamsmap = Paramsmap;
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
     *
     * @param activity
     * @param url
     * @param params
     * @param callback
     */
    public void Post(final Activity activity, String url, Map<String, String> params, final ResponseCallback callback) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
            if (mParamsmap != null) {//覆盖全局请求事先约定的参数
                for (String key : mParamsmap.keySet()) {
                    formBodyBuilder.add(key, mParamsmap.get(key));
                }
            }
        }
        Request request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (activity != null && !activity.isFinishing())
                    activity.runOnUiThread(new Runnable() {//恕我愚钝，本在旧框架的问题，在返回后有些耗时操作（gson）也放到了主线程，也不敢加上Rxjava，智只能祈祷不要太耗时
                        @Override
                        public void run() {
                            Logs.e("onFailure", "in okhttp");
                            if (e instanceof SocketTimeoutException) {
                                callback.OnFail("-888", "网络连接超时，请稍候再试");
                            } else if (e instanceof ConnectException) {
                                callback.OnFail("-800", "网络连接错误，请稍候再试");
                            } else if (e instanceof UnknownHostException) {
                                callback.OnFail("-800", "网络连接错误，请稍候再试");
                            } else if (e instanceof UnknownHostException) {
                                callback.OnFail("-888", "无法连接到服务器,请稍候再试");
                            } else {
                                callback.OnFail("-900", e.getMessage());
                            }
                        }
                    });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String str = response.body().string();
                if (activity != null && !activity.isFinishing())

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Logs.i("onResponse", str);
                            if (response.isSuccessful())
                                callback.OnSuccess(str);
                            else
                                callback.OnFail("" + response.code(), response.message());
                        }
                    });

            }

        });
    }


    /**
     * 兼容老版本的post请求，返回为字符串类型
     *
     * @param activity
     * @param url
     * @param params
     * @param callback
     * @param isEncrypt 是否加密
     */
    public void Post(final Activity activity, String url, Map<String, String> params, boolean isEncrypt, final ResponseCallback callback) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        JSONObject jsonObject = new JSONObject();


        if (isEncrypt) {
            if (params != null && params.size() > 0) {
                if (mParamsmap != null) {//覆盖全局请求事先约定的参数
                    params.putAll(mParamsmap);
                }
                Set<Map.Entry<String, String>> entrySet = params.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    try {
                        jsonObject.put(entry.getKey(), entry.getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //formBodyBuilder.add(entry.getKey(), entry.getValue());
                formBodyBuilder.add("pdvs", EncryptUtil.getEncryptValue(jsonObject.toString()));
                formBodyBuilder.add("sign", EncryptUtil.getEncryptSign(jsonObject.toString()));
            }
        } else {
            if (params != null && params.size() > 0) {
                if (mParamsmap != null) {//覆盖全局请求事先约定的参数
                    params.putAll(mParamsmap);
                }
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
                if (activity != null && !activity.isFinishing())
                    activity.runOnUiThread(new Runnable() {//恕我愚钝，本在旧框架的问题，在返回后有些耗时操作（gson）也放到了主线程，也不敢加上Rxjava，智只能祈祷不要太耗时
                        @Override
                        public void run() {
                            Logs.e("onFailure", "in okhttp");
                            if (e instanceof SocketTimeoutException) {
                                callback.OnFail("-888", "网络连接超时，请稍后重试");
                            } else if (e instanceof ConnectException) {
                                callback.OnFail("-800", "网络连接错误，请稍后重试");
                            } else {
                                callback.OnFail("-900", e.getMessage());
                            }
                        }
                    });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String str = response.body().string();
                if (activity != null && !activity.isFinishing())

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Logs.i("onResponse", str);
                            if (response.isSuccessful())
                                callback.OnSuccess(str);
                            else
                                callback.OnFail("" + response.code(), response.message());
                        }
                    });

            }

        });


    }


    /**
     * 说实话 我懒的写同步get请求，你不放formbody他就是get请求
     */
    public void Get() {

    }


    //测试apk http://dl.wandoujia.com/files/jupiter/latest/wandoujia-web_seo_baidu_homepage.apk

    /**
     * 无参下载文件
     *
     * @param fileUrl     文件url
     * @param destFileDir 存储目标目录
     */
    public <T> void downLoadFile(String fileUrl, final String destFileDir, String filename, final MyFileRequestCallback<T> callBack) {
        //final String fileName = MD5.encode(fileUrl);
        final File file1 = new File(destFileDir);
        if (!file1.exists()) {
            file1.mkdir();
        }
        long downloadLength = 0;   //记录已经下载的文件长度
        final File file = new File(file1.getAbsolutePath() + "/" + filename);
        this.filename = filename;
        if (file.exists()) {//防止未下载完 的包 直接删除重新下 断点续传有风险哦
            file.delete();
               /* downloadLength=file.length();
                if(getContentLength(fileUrl)==downloadLength){
                    callBack.OnSuccess(file);
                    return;
                }else{
                    final Request request = new Request.Builder().url(fileUrl).addHeader("Accept-Encoding", "identity").addHeader("RANGE","bytes="+downloadLength+"-").build();
                }*/
        }


        final Request request = new Request.Builder().url(fileUrl).addHeader("Accept-Encoding", "identity").build();
        //很坑啊 okhttp默认的接受模式 是gzip压缩过的 所以拿不到contentlenght
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logs.e("onFailure", e.toString());

                callBack.OnFail("onFailure", "下载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    long total_cut = total / 50;
                    long currentpercent = 0;

                    Log.e("request", "total------>" + total);
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        // Log.e("onResponse", "current------>" + current);
                        if (current > currentpercent) {//当有进度更新时才触发回调
                            currentpercent = currentpercent + total_cut;
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


    /**
     * 事先得到下载内容的大小
     *
     * @param downloadUrl
     * @return
     */
    private long getContentLength(String downloadUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).addHeader("Accept-Encoding", "identity").build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.body().close();
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static SSLParams getSslSocketFactory(InputStream[] certificates, InputStream bksFile, String password) {
        SSLParams sslParams = new SSLParams();
        try {
            TrustManager[] trustManagers = HttpsManager.prepareTrustManager(certificates);
            KeyManager[] keyManagers = HttpsManager.prepareKeyManager(bksFile, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager trustManager = null;
            if (trustManagers != null) {
                trustManager = new MyTrustManager(chooseTrustManager(trustManagers));
            } else {
                trustManager = new UnSafeTrustManager();
            }
            sslContext.init(keyManagers, new TrustManager[]{trustManager}, null);
            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = trustManager;
            return sslParams;
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        } catch (KeyManagementException e) {
            throw new AssertionError(e);
        } catch (KeyStoreException e) {
            throw new AssertionError(e);
        }
    }


    /**
     * 设置证书
     *
     * @param certificates
     */
    public void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            // mOkHttpClient.sslSocketFactory().setSslSocketFactory(sslContext.getSocketFactory());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//蛋疼哦  bmob乱搞哦 rxjava2冲突哦 然后就分包失败哦 然后就是懒得重新改回rxjava哦
//    /**
//     * 接下来的部分是 结合 rxjava2 写的请求 本身你可以看到在上述的post请求中 引用到了activity，当然不知名高并发的情况下会出现内存泄漏问题
//     * 然后我并不知道rxjava源码实现切换线程原理，就从劫持activity上面来说，rxjava不用你引用activity对象
//     */
//    public void Post(String url, Map<String, String> params, boolean isEncode, final ResponseCallback callback) {
//        Post(url, params, false, isEncode, callback);
//    }
//
//
//    //无activity调用,此处需要注意的是 该处回调可以在子线程操作，不要忘记切换后再进行UI操作，这个方法适用于完全无ui操作的类型
//    public void Post(String url, Map<String, String> params, boolean callbackinIo, boolean isEncode, final ResponseCallback callback) {
//        Post(url, params, isEncode)
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        if (callback instanceof ProgressCallBack && callback != null)
//                            ((ProgressCallBack) callback).OnStart();
//                    }
//                })
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(callbackinIo ? Schedulers.io() : AndroidSchedulers.mainThread())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.e("onSubscribe", "--" + d);
//                    }
//
//                    @Override
//                    public void onNext(String value) {
//                        Log.e("onNext", "--" + value);
//                        callback.OnSuccess(value);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("onError", "--");
//                        if (e instanceof SocketTimeoutException) {
//                            callback.OnFail("-888", "网络连接超时，请稍候再试");
//                        } else if (e instanceof ConnectException) {
//                            callback.OnFail("-800", "网络连接错误，请稍候再试");
//                        } else if (e instanceof UnknownHostException) {
//                            callback.OnFail("-800", "网络连接错误，请稍候再试");
//                        } else if (e instanceof UnknownHostException) {
//                            callback.OnFail("-888", "无法连接到服务器,请稍候再试");
//                        } else {
//                            callback.OnFail("-900", e.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        callback.OnFinsh();
//                    }
//                });
//    }
//
//    public void UpLoadFile( String filepath,String upurl, HashMap<String, String> params, final ResponseCallback callback) {
//        Post(upurl, filepath, params, true)
//        /*        .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        if(callback instanceof ProgressCallBack &&callback!=null)
//                            ((ProgressCallBack) callback).OnStart();
//                    }
//                })
//                .subscribeOn(AndroidSchedulers.mainThread())*/
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.e("onSubscribe", "--" + d);
//                    }
//
//                    @Override
//                    public void onNext(String value) {
//                        Log.e("onNext", "--" + value);
//                        callback.OnSuccess(value);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("onError", "--");
//                        if (e instanceof SocketTimeoutException) {
//                            callback.OnFail("-888", "网络连接超时，请稍候再试");
//                        } else if (e instanceof ConnectException) {
//                            callback.OnFail("-800", "网络连接错误，请稍候再试");
//                        } else if (e instanceof UnknownHostException) {
//                            callback.OnFail("-800", "网络连接错误，请稍候再试");
//                        } else if (e instanceof UnknownHostException) {
//                            callback.OnFail("-888", "无法连接到服务器,请稍候再试");
//                        } else {
//                            callback.OnFail("-900", e.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        callback.OnFinsh();
//                    }
//                });
//    }
//
//
//    /**
//     * 【拼装参数请求体
//     *
//     * @param params
//     * @param isEncode
//     * @return
//     */
//    private FormBody.Builder Encode(Map<String, String> params, boolean isEncode) {
//        FormBody.Builder formBodyBuilder = new FormBody.Builder();
//        JSONObject jsonObject = new JSONObject();
//        if (isEncode) {
//            if (params != null && params.size() > 0) {
//                if (mParamsmap != null) {//覆盖全局请求事先约定的参数
//                    params.putAll(mParamsmap);
//                }
//                Set<Map.Entry<String, String>> entrySet = params.entrySet();
//                for (Map.Entry<String, String> entry : entrySet) {
//                    try {
//                        jsonObject.put(entry.getKey(), entry.getValue());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                formBodyBuilder.add("pdvs", EncryptUtil.getEncryptValue(jsonObject.toString()));
//                formBodyBuilder.add("sign", EncryptUtil.getEncryptSign(jsonObject.toString()));
//            }
//        } else {
//            if (params != null && params.size() > 0) {
//                if (mParamsmap != null) {//覆盖全局请求事先约定的参数
//                    params.putAll(mParamsmap);
//                }
//                Set<Map.Entry<String, String>> entrySet = params.entrySet();
//                for (Map.Entry<String, String> entry : entrySet) {
//                    formBodyBuilder.add(entry.getKey(), entry.getValue());
//                }
//            }
//        }
//        return formBodyBuilder;
//    }
//
//    /**
//     * 【拼装复合参数请求体
//     *
//     * @param params
//     * @param isEncode
//     * @return
//     */
//    private MultipartBody.Builder Encode(Map<String, String> params, String filepath, boolean isEncode) {
//        File file = new File(filepath);
//        if(file.length()>1*1024*1024){
//            file= FileUtil.compressImage(filepath);
//        }
//        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), file);
//        String file1Name = "upload.png";
//        MultipartBody.Builder formBodyBuilder = new MultipartBody.Builder();
//        JSONObject jsonObject = new JSONObject();
//        if (!isEncode) {
//            if (params != null && params.size() > 0) {
//                if (mParamsmap != null) {//覆盖全局请求事先约定的参数
//                    params.putAll(mParamsmap);
//                }
//                Set<Map.Entry<String, String>> entrySet = params.entrySet();
//                for (Map.Entry<String, String> entry : entrySet) {
//                    formBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
//                }
//            }
//        } else {
//            if (params != null && params.size() > 0) {
//                if (mParamsmap != null) {//覆盖全局请求事先约定的参数
//                    params.putAll(mParamsmap);
//                }
//                Set<Map.Entry<String, String>> entrySet = params.entrySet();
//                for (Map.Entry<String, String> entry : entrySet) {
//                    try {
//                        jsonObject.put(entry.getKey(), entry.getValue());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                //formBodyBuilder.add(entry.getKey(), entry.getValue());
//                formBodyBuilder.addFormDataPart("pdvs", EncryptUtil.getEncryptValue(jsonObject.toString()));
//                formBodyBuilder.addFormDataPart("sign", EncryptUtil.getEncryptSign(jsonObject.toString()));
//            }
//        }
//        formBodyBuilder.setType(MultipartBody.FORM)
//                .addFormDataPart("uploadfile", file1Name, fileBody1)
//                .build();
//        return formBodyBuilder;
//    }


//    private Observable<String> Post(final String url, final String filepath, final Map<String, String> params, final boolean encode) {
//        return Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(final ObservableEmitter<String> subscriber) throws Exception {
//                Request request = new Request.Builder().url(url).post(Encode(params, filepath, encode).build()).build();
//                Call call = mOkHttpClient.newCall(request);
//                call.enqueue(new Callback() {
//
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        subscriber.onError(e);
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if (response.isSuccessful()) {
//                            subscriber.onNext(response.body().string());
//                        }
//                        subscriber.onComplete();
//                    }
//                });
//            }
//        });
//    }
//
//    private Observable<String> Post(final String url, final Map<String, String> params, final boolean encode) {
//        return Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(final ObservableEmitter<String> subscriber) throws Exception {
//                final Request request = new Request.Builder().url(url).post(Encode(params, encode).build()).build();
//                Call call = mOkHttpClient.newCall(request);
//
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        subscriber.onError(e);
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if (response.isSuccessful()) {
//                            subscriber.onNext(response.body().string());
//                        }
//                        subscriber.onComplete();
//                    }
//                });
//
//            }
//        });
//    }
//
//
//    /**
//     * 此接口用来做轮询的操作
//     *
//     * @param url
//     * @param params
//     * @param isEncode
//     * @param callback
//     */
//    public void Polling(String url, Map<String, String> params, boolean isEncode, final PollingCallback callback) {
//        Post(url, params, isEncode)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.e("onSubscribe", "--" + d);
//                    }
//
//                    @Override
//                    public void onNext(String value) {
//                        Log.e("onNext", "--" + value);
//                        callback.OnSuccess(value);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("onError", "--");
//                        if (e instanceof SocketTimeoutException) {
//                            callback.OnFail("-888", "网络连接超时，请稍候再试");
//                        } else if (e instanceof ConnectException) {
//                            callback.OnFail("-800", "网络连接错误，请稍候再试");
//                        } else if (e instanceof UnknownHostException) {
//                            callback.OnFail("-800", "网络连接错误，请稍候再试");
//                        } else if (e instanceof UnknownHostException) {
//                            callback.OnFail("-888", "无法连接到服务器,请稍候再试");
//                        } else {
//                            callback.OnFail("-900", e.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        callback.OnFinsh();
//                    }
//                });
//    }
//
//
//    /**
//     * 轮询请求接口，这里有个思路问题，我们需要在分清 定时和接受再请求的情况与区别，主体思想区别就是interval和takeutil
//     *
//     * @param url
//     * @param params
//     * @param encode
//     * @return 被观察者的本体
//     */
//    private Observable<String> Poll(final String url, final Map<String, String> params, final boolean encode) {
//        return Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(final ObservableEmitter<String> subscriber) throws Exception {
//                final Request request = new Request.Builder().url(url).post(Encode(params, encode).build()).build();
//                Call call = mOkHttpClient.newCall(request);
//
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        subscriber.onError(e);
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if (response.isSuccessful()) {
//                            subscriber.onNext(response.body().string());
//                        }
//                        subscriber.onComplete();
//                    }
//                });
//            }
//        });
//    }
}
