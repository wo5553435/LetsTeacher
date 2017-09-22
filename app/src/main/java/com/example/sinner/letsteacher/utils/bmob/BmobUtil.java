package com.example.sinner.letsteacher.utils.bmob;

import android.os.Environment;
import android.util.Log;

import com.example.sinner.letsteacher.utils.Logs;
import com.example.sinner.letsteacher.utils.bmob.listener.data.BaseBmobListener;
import com.example.sinner.letsteacher.utils.bmob.listener.data.BaseBmobMultListener;
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobAddOrUpdateListener;
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobQueryListener;
import com.example.sinner.letsteacher.utils.bmob.listener.file.BmobDownListener;
import com.example.sinner.letsteacher.utils.bmob.listener.file.BmobFileListener;
import com.example.sinner.letsteacher.utils.bmob.listener.file.BmobMulitFileListener;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DeleteBatchListener;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/** 说明下 这个文档 因为bmob的更新问题 有的接口不一定使用 或者已经失效 抑或写完一直没用缺过时了
 *
 * Created by win7 on 2016-12-27.
 */

public class BmobUtil {


    private static BmobUtil ourInstance = new BmobUtil();

    public static BmobUtil getInstance() {
        return ourInstance;
    }

    private BmobUtil() {
    }
    private BmobQuery query;



    public<T> void CountData(T tablename, Map<String,Object> params, CountListener listener){
        query=new BmobQuery();
        if(params!=null){
            for (String key:params.keySet()) {
                query.addWhereEqualTo(key,params.get(key));
            }
        }
        query.count(tablename.getClass(),listener);
    }

    /**
     * 查询数据獲得符合条件的数据 适用于单一文档
     * 注意  官方文档已更新 建议用泛型
     */
    public <T> void  SearchData(T  tablename, Map<String,String> params, final BmobQueryListener listener){
        query =new BmobQuery<T>();
        if(params!=null){
            for (String key:params.keySet()) {
                query.addWhereEqualTo(key,params.get(key));
            }
        }

        query.setLimit(10);
        query.order("createdAt");
        query.findObjects(listener);
    }


    /**
     * 查询数据獲得符合条件的数据 适用于单一文档
     * 注意  官方文档已更新 建议用泛型
     */
    public <T> void  SearchData(T  tablename, Map<String,String> params, Map<String,String> limit, final BmobQueryListener listener){
        query =new BmobQuery<T>();
        if(params!=null){
            for (String key:params.keySet()) {
                query.addWhereEqualTo(key,params.get(key));
            }
        }
        if(limit!=null){
            for(String key:limit.keySet()){
                query.addWhereNotEqualTo(key,limit.get(key));
            }
        }
        query.setLimit(10);
        query.order("createdAt");
        query.findObjects(listener);
    }


    /**
     * 分页查询结果
     * @param tablename
     * @param params
     * @param currentpage
     * @param pagesize
     * @param listener
     * @param <T>
     */
    public <T> void  SearchDataByLimit(T  tablename, Map<String,String> params, int currentpage,int pagesize,final BmobQueryListener listener){
        query =new BmobQuery<T>();
        if(params!=null){
            for (String key:params.keySet()) {
                query.addWhereEqualTo(key,params.get(key));
            }
        }

        query.setLimit(pagesize);
        if(currentpage>1)
        query.setSkip(pagesize*(currentpage-1));
        query.order("createdAt");
        query.findObjects(listener);
    }

    public <T> void  SearchDataByLimit(T  tablename, Map<String,String> params, int currentpage,final BmobQueryListener listener){
        SearchDataByLimit(tablename, params, currentpage,20, listener);
    }


    /**
     * 删除d单一文件
     * @param file
     */
    public void DeleteFile(BmobFile file, final BmobFileListener listener){
        if(file!=null&&file.getUrl()!=null){
            file.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        listener.OnSuccess("文件删除成功");
                    }else{
                        listener.OnFail("文件删除失败："+e.getErrorCode(),""+e.getMessage());
                    }
                }
            });
        }
    }

    /**
     * 批量删出文件
     * @param urls
     * @param listener
     */
    public void DeleteFiles(String[] urls, final BmobFileListener listener){//官方文档好像出现了一点问题 返回好像确实出现了强转问题
        BmobFile.deleteBatch(urls, new DeleteBatchListener() {
            @Override
            public void done(String[] failurls, BmobException e) {

                if(e==null){
                    listener.OnSuccess("全部删除成功");
                }else{
                    e.printStackTrace();
                    if(failurls!=null){
                        listener.OnFail("删除失败",failurls.length+","+e.toString());
                    }else{
                        listener.OnFail("全部文件删除失败:",e.getErrorCode()+","+e.toString());
                    }
                }
            }
        });
    }

    /**
     * 向指定表插入一条新数据，理论上是需要查重判断
     * @param object 插入类型
     * @param tablename 表名
     * @param params  需要匹配的字段关键字 特别需要注意的是 参数需要你特别注意并不要指定为int的基本型，请使用Integer等封装类型
     */
    public void addDataCheck(final BmobObject object, final String tablename, Map<String,Object> params,final BaseBmobListener listener){
        query =new BmobQuery(tablename);
        if(params!=null){
            for (String key:params.keySet()) {
                query.addWhereEqualTo(key,params.get(key));
            }
        }
        query.order("createdAt");
        //v3.5.0版本提供findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
               if(e==null){//正确情况下的输出

                if(ary.length()==0){//没问题，没有重复条件数据
                    addData(object,listener);
                }else{//自定义错误
                    listener.onDone("Error"+"该账号已存在!",e);
                }
               }else{//完全有问题的查询
                   listener.onDone("系统繁忙!请稍后再试",e);
               }
            }
        });
    }

    public  void addData(BmobObject object,final BaseBmobListener listener){
          object.save(new SaveListener<String>() {
              @Override
              public void done(String s, BmobException e) {
                  listener.onDone(s,e);
              }
          });
    }

    /**
     * 获得单一指定数据的数据,通常是根据idtoken查
     */
    public void queryDataByid(BmobObject objects,String id){
        BmobQuery<BmobObject> bmobQuery = new BmobQuery<BmobObject>();
        bmobQuery.getObject( id, new QueryListener<BmobObject>() {
            @Override
            public void done(BmobObject object,BmobException e) {
                if(e==null){

                }else{

                }
            }
        });
    }



    /**
     * 单个 修改固定表中的值或代替整体,注意，这个object需要是已经存在云端的数据对象
     */
    public void updateEntity(BmobObject object, Map<String,Object> updateparams, final BmobAddOrUpdateListener listener){
        if(object!=null){
            for(String s:updateparams.keySet()){
                object.setValue(s,updateparams.get(s));
            }

            object.update(object.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        listener.OnSuccess("提交数据成功");
                    }else{
                        listener.OnFail(e.getMessage());
                    }
                }
            });
        }else{
            listener.OnFail("未指定提交数据");
        }
    }

    public  void UpdataObjects(List<BmobObject> objs, final BaseBmobMultListener listener){
        new BmobBatch().updateBatch(objs).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if(e==null){
                    for(int i=0;i<list.size();i++){
                        BatchResult result = list.get(i);
                        List<Integer> error=new ArrayList<Integer>();
                        BmobException ex =result.getError();
                        if(ex==null){
                            Logs.e("第"+i+"个数据批量更新成功：",""+result.getUpdatedAt());
                        }else{
                            error.add(i);
                            Logs.e("第"+i+"个数据批量更新失败："+ex.getMessage(),""+ex.getErrorCode());
                        }

                        if(error.size()!=0) listener.onSuccess(error);
                        else listener.onSuccess();

                    }
                }else{
                    listener.onFailure(""+e.getErrorCode(),e.getMessage());
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }


    /**
     * 直接修改指定对象的属性，指定到参数（特指到整数，数字型） 单个
     */
    public void updateItemOfObject(BmobObject obj ,String params ,int num,final BmobAddOrUpdateListener listener){
        if(obj!=null){
            obj.increment(params,num);
            obj.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null)//没问题的操作
                    listener.OnSuccess("操作成功");
                    else listener.OnFail(e.getMessage());
                }
            });
        }else{
            listener.OnFail("未指定提交数据");
        }
    }

    /**
     * 直接修改指定对象的属性，指定到参数（特指到整数，数字型） 单个
     */
    public  void updateItemOfObject(BmobObject obj ,Map<String,Integer> params ,int num,final BmobAddOrUpdateListener listener){
        if(obj!=null){
            for(String key:params.keySet()){
                obj.increment(key,params.get(key));
            }

            obj.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null)
                        listener.OnSuccess("操作成功");
                    else listener.OnFail(e.getMessage());
                }
            });
        }else{
            listener.OnFail("未指定提交数据");
        }
    }


    /**
     * 删除云端指定对象 该对象应为已有数据
     * @param object
     * @param listener
     */
    public void DeleteObject(BmobObject object,final  BmobAddOrUpdateListener listener){
        if(object!=null){
            object.delete(object.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null) listener.OnSuccess("操作成功");
                    else listener.OnFail(e.getMessage());
                }
            });

        }else{
            listener.OnFail("未指定提交数据");
        }
    }

    public void DeleteObjects(List<BmobObject> objects, final BaseBmobMultListener listener){
        new BmobBatch().deleteBatch(objects).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if(e==null){
                    List<Integer> errorindex=new ArrayList<Integer>();
                    for(int i=0;i<list.size();i++){
                        BatchResult result = list.get(i);
                        BmobException ex =result.getError();
                        if(ex==null){
                            Logs.e("deleteobjs","第"+i+"个数据批量删除成功");
                        }else{
                            errorindex.add(i);
                            Logs.e("deleteobjs","第"+i+"个数据批量删除失败："+ex.getMessage()+","+ex.getErrorCode());
                        }
                    }
                    if(errorindex.size()==0) {
                        listener.onSuccess();
                    }
                    else {
                        listener.onSuccess(errorindex);
                    }
                }else{
                    e.printStackTrace();
                    listener.onFailure(""+e.getErrorCode(),e.getMessage());
                    Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }


    //以下函数多为批量操作，请注意范围和参数和是否带回滚

    /**
     * 批量增加同一表中的对象
     * @param Action   是否需要回滚事务 操作失误的不去管
     * @param objects   需要添加的对象集合
     * @param listener 回调监听
     */
    public void InsertBatch(boolean Action,List<BmobObject> objects,final BaseBmobMultListener listener){

        if(Action){//有问题的批量
            if(objects!=null&&objects.size()>0){
//                Class<? extends  BmobObject> classoft=objects.get(0).getClass();
//                ParameterizedType parameterizedType = (ParameterizedType) objects.get(0).getClass().getGenericSuperclass();//获取当前new对象的泛型的父类类型
//                int index = 0;//第n个泛型    Map<K,V> 就有2个  拿K  就是0  V就是1
//                Class clazz = (Class<? extends  BmobObject>) parameterizedType.getActualTypeArguments()[index];
//                (clazz.getClass()) a=new
//                System.out.println("clazz ==>> "+clazz);
                BmobObject o=new BmobObject();
            }
        }else{
             new BmobBatch().insertBatch(objects).doBatch(new QueryListListener<BatchResult>() {

                             @Override
                             public void done(List<BatchResult> o, BmobException e) {
                                 if(e==null){
                                     List<Integer> error=new ArrayList<Integer>();
                                     Logs.e("开始循环遍历","----");
                                     for(int i=0;i<o.size();i++){
                                         BatchResult result = o.get(i);
                                         BmobException ex =result.getError();
                                         if(ex==null){

                                             Logs.e("第"+i+"个数据批量添加成功："+result.getCreatedAt(),"+"+result.getObjectId()+",+"+result.getUpdatedAt());
                                         }else{
                                             error.add(i);
                                             // listener.onFailure(""+ex.getErrorCode(),ex.getMessage());
                                             Logs.e("第"+i+"个数据批量添加失败："+ex.getMessage(),""+ex.getErrorCode());
                                         }
                                     }
                                     if(error.size()!=0){
                                         listener.onSuccess(error);
                                     }else{
                                     listener.onSuccess();
                                     }
                                 }else{
                                     Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                     listener.onFailure(""+e.getErrorCode(),e.getMessage());
                                 }
                             }
                         });

        }
    }



    /**
     * 上传单一文件
     * @param filepath 本地文件路径
     * @param file 保存载体
     * @param listener 回调
     */
    public void UploadFile(String filepath, BmobFile file, final BmobFileListener listener){
        final BmobFile bf=new BmobFile(new File(filepath));
        bf.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){//正确情况下
                    listener.OnSuccess(bf.getFileUrl());
                }else{
                    listener.OnFail(""+e.getErrorCode(),e.getMessage());
                }
            }
        });

    }

    /**
     * 批量上传文件
     * @param listener
     * @param filepaths
     */
    public void UploadFiles(final BmobMulitFileListener listener, final String... filepaths){
        BmobFile.uploadBatch(filepaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files,List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if(urls.size()==filepaths.length){//如果数量相等，则代表文件全部上传完成
                    //do something
                    listener.OnSuccess(files,urls,0);
                }else{
                   // listener.OnSuccess(files,urls,(filepaths.length-urls.size()));
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                listener.OnFail(""+statuscode,errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                listener.OnProgress(curIndex,total);
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
    }

    /**
     * 下载文件
     * @param bf
     * @param filepath
     * @param listener
     */
    public void DownLoadFile(BmobFile bf, String filepath, final BmobDownListener listener){
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(Environment.getExternalStorageDirectory(), bf.getFilename());
        bf.download(saveFile, new DownloadFileListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
                   listener.OnSuccess("下载成功,保存路径:"+savePath);
                }else{
                    listener.OnFail(e.getErrorCode(),e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                listener.OnProgress(value,100);
            }

        });

    }

}
