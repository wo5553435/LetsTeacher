package com.example.sinner.letsteacher.activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.adapter.FilesearchedAdapter;
import com.example.sinner.letsteacher.entity.FileBean;
import com.example.sinner.letsteacher.entity.ImageVo;
import com.example.sinner.letsteacher.entity.LoginUserEntity;
import com.example.sinner.letsteacher.utils.Logs;
import com.example.sinner.letsteacher.utils.StringTools;
import com.example.sinner.letsteacher.utils.SuperToastUtil;
import com.example.sinner.letsteacher.utils.bmob.BmobUtil;
import com.example.sinner.letsteacher.utils.bmob.listener.data.BaseBmobMultListener;
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobQueryListener;
import com.example.sinner.letsteacher.utils.bmob.listener.file.BmobFileListener;
import com.example.sinner.letsteacher.utils.bmob.listener.file.BmobMulitFileListener;
import com.example.sinner.letsteacher.views.dialog.ActionDialog;
import com.example.sinner.letsteacher.views.dialog.NetFileDirsDialog;
import com.jay.ui.PhotoPickerActivity;
import com.library.xrecyclerview.ProgressStyle;
import com.library.xrecyclerview.XRecyclerView;
import com.pigcms.library.android.okhttp.HttpUtils;
import com.pigcms.library.android.okhttp.callback.MyRequestCallback;
import com.pigcms.library.view.dialog.MyDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sinner on 2017-08-23.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class ShowNetPicActivity extends BasicActivity {

    @BindView(R.id.rv_searchfile_data)
    XRecyclerView rv_filedata;

    @BindView(R.id.img_search_progress)
    AppCompatImageView img_progress;

    @BindView(R.id.title_righttext)
    TextView tv_right;
    @BindView(R.id.title_text)
    TextView tv_title;

    @BindView(R.id.title_lin_back)
    LinearLayout layout_back;

    @BindView(R.id.fab)
    FloatingActionButton fab;


    FilesearchedAdapter adapter;
    ArrayList<String> data;
    List<ImageVo> data_file = new ArrayList<>();
    AnimationSet animationSet;
    Animation closeanimation;
    String groupid;
    boolean hasmore = true;
    int currentpage = 1;

    @Override
    protected int getContentLayout() {
        return R.layout.layout_activity_searchfile;
    }

    @Override
    protected void initGui() {
        tv_title.setText(StringTools.INSTANCE.getNotNullStr(getIntent().getStringExtra("name"), "图记"));
        tv_right.setVisibility(View.GONE);
        tv_right.setText("操作");
    }

    @Override
    protected void initData() {
        // initTransition();
        groupid = getIntent().getStringExtra("id");
        initAnimationSet();
        //initSRLayout();
        initRv();
        SearchFile(true);
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0://进入刷新状态
                   // srlayout.setRefreshing(true);
                    showFab(false);
                    break;
                case 1://结束刷新状态
                    //srlayout.setRefreshing(false);
                    rv_filedata.refreshComplete();
                    showFab(true);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 展示或者隐藏上传按钮
     *
     * @param isshow
     */
    private void showFab(final boolean isshow) {
        float end = isshow ? 1.0f : 0.0f;
        ViewCompat.animate(fab).scaleX(end).scaleY(end).setDuration(500).setInterpolator(new FastOutSlowInInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        if (isshow) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        if (!isshow) {
                            view.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                })
                .start();
    }

    /**
     * 初始化transition
     */
    private void initTransition() {
        getWindow().setEnterTransition(initContentEnterTransition());
        getWindow().setSharedElementEnterTransition(initSharedElementEnterTransition());
        getWindow().setReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.return_slide));

    }

    private Transition initContentEnterTransition() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_and_fade);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
//                liney_bottom.setTransitionGroup(true);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        return transition;
    }

    private Transition initSharedElementEnterTransition() {

        final Transition sharedTransition = new TransitionSet().addTransition(new ChangeBounds().setDuration(500));
        sharedTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        return sharedTransition;
    }

    @OnClick({R.id.fab, R.id.title_righttext})
    public void OnClickListener(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (groupid != null && groupid.length() != 0) {
                    Intent intent = new Intent(activity, PhotoPickerActivity.class);
//                    intent.putExtra("id",groupid);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(PhotoPickerActivity.IS_MULTI_SELECT, true);
                    bundle.putInt(PhotoPickerActivity.MAX_SELECT_SIZE, 10);

                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1001);
                }
                break;
            case R.id.title_righttext:
                if (adapter.getSelect() != null && adapter.getSelect().size() != 0) {
                    ShowAction();
                }
//                BmobFile.deleteBatch(new String[]{"http://bmob-cdn-13614.b0.upaiyun.com/2017/09/05/b61b8b6aceb4490bad946b620bd8e151.jpg"}, new DeleteBatchListener() {
//                    @Override
//                    public void done(String[] strings, BmobException e) {
//                        if(e==null){
//                            //listener.OnSuccess("全部删除成功");
//                        }else{
//                            e.printStackTrace();
//                            if(strings!=null){
//                               // listener.OnFail("删除失败",failurls.length+","+e.toString());
//                            }else{
//                               // listener.OnFail("全部文件删除失败:",e.getErrorCode()+","+e.toString());
//                            }
//                        }
//                    }
//                });

                break;
        }
    }

    /**
     * 展现操作菜单
     */
    private void ShowAction() {
        final ActionDialog dialog = new ActionDialog(activity, R.style.DialogStyle);
        dialog.SetListener(new ActionDialog.OnActionClick() {
            @Override
            public void OnItemClick(int position) {
                switch (position) {
                    case 1:// 批量下载
                        break;
                    case 2://复制
                        getNetFileDirs(1);
                        dialog.dismiss();
                        break;
                    case 3://粘贴
                        getNetFileDirs(2);
                        dialog.dismiss();
                        break;
                    case 4://删除
                        dialog.dismiss();
                        final MyDialog dialog1 = new MyDialog(activity, R.style.DialogStyle);
                        dialog1.setTextContent("确认删除所选项？");
                        dialog1.setOnlyOk(false);
                        dialog1.setCanceledOnTouchOutside(true);
                        dialog1.setOnResultListener(new MyDialog.OnResultListener() {
                            @Override
                            public void Ok() {
                                DeleteUserimgData();
                                dialog1.dismiss();
                            }

                            @Override
                            public void Cancel() {
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                        break;
                }
            }
        });
        dialog.show();
    }

    /**
     * 获取
     */
    public void getNetFileDirs(final int type) {
        showPDialog();
        HashMap<String, String> limit = new HashMap<>();
        limit.put("groupid", groupid);
        BmobUtil.getInstance().SearchData(FileBean.class, null, limit, new BmobQueryListener<FileBean>() {

            @Override
            public void OnSuccess(FileBean object) {

            }

            @Override
            public void OnSuccess(List<FileBean> objects) {
                Logs.e("objects", "---" + objects.size());
                hidePDialog();
                if (objects != null) ShowNetFileDirs(objects, type);
            }

            @Override
            public void OnFail(String code, String error) {
                hidePDialog();
                SuperToastUtil.getInstance(activity).showToast("查找错误!" + error);
            }
        });
    }

    public void ShowNetFileDirs(final List<FileBean> data, final int type) {
        final NetFileDirsDialog dialog = new NetFileDirsDialog(activity, data, R.style.DialogStyle);
        dialog.setListener(new NetFileDirsDialog.ActionClick() {
            @Override
            public void onItemClick(int position) {
                Logs.e("点击的是", "");
                String groupids = data.get(position).getGroupid();
                if (type == 1) {//复制到
                    Ctrl_V(groupids);
                } else if (type == 2) {//剪切到
                    UpdateFilesDirs(groupids);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 复制 简单来说就是批量添加
     */
    public void Ctrl_V(String groupid) {
        List<Integer> index = adapter.getSelect();
        List<BmobObject> files = new ArrayList<>();
        for (int i = 0; i < index.size(); i++) {
            ImageVo img = new ImageVo();
            img.setObjectId(data_file.get(index.get(i)).getObjectId());
            img.setGroup(groupid);
            files.add(img);
        }
        showPDialog();
        BmobUtil.getInstance().InsertBatch(false, files, new BaseBmobMultListener() {
            @Override
            public void onSuccess() {
                hidePDialog();
                SuperToastUtil.getInstance(activity).showToast("操作成功!");
                Refresh();
            }

            @Override
            public void onFailure(String errorcode, String errormsg) {
                hidePDialog();
                SuperToastUtil.getInstance(activity).showToast("操作失败!错误码:" + errorcode);
                Refresh();
            }

            @Override
            public void onSuccess(List<Integer> errorcount) {
                hidePDialog();
                SuperToastUtil.getInstance(activity).showToast("操作冲突!有" + errorcount.size() + "项位未操作");
                Refresh();
            }
        });
    }


    /**
     * 剪切 简单来说就是更新数据
     *
     * @param groupid
     */
    public void UpdateFilesDirs(String groupid) {
        List<Integer> index = adapter.getSelect();
        List<BmobObject> files = new ArrayList<>();
        for (int i = 0; i < index.size(); i++) {
            ImageVo img = new ImageVo();
            img.setObjectId(data_file.get(index.get(i)).getObjectId());
            img.setGroup(groupid);
            files.add(img);
        }
        showPDialog();
        BmobUtil.getInstance().UpdataObjects(files, new BaseBmobMultListener() {
            @Override
            public void onSuccess() {
                hidePDialog();
                SuperToastUtil.getInstance(activity).showToast("操作成功");
                Refresh();
            }

            @Override
            public void onFailure(String errorcode, String errormsg) {
                hidePDialog();
                SuperToastUtil.getInstance(activity).showToast("操作失败" + errormsg);
                Refresh();
            }

            @Override
            public void onSuccess(List<Integer> errorcount) {
                hidePDialog();
                Refresh();
            }
        });
    }

    /**
     * 将
     */
    private void UpdateUser() {

    }


    /**
     * 删除修改文件表中
     */
    private void DeleteUserimgData() {

        final List<BmobObject> selects = new ArrayList<>();
        final List<String> selecturls = new ArrayList<>();
        final List<Integer> indexs = adapter.getSelect();
        for (int i = 0; i < indexs.size(); i++) {
            Logs.e("adapter select", "" + indexs.get(i));
            Logs.e("将要删除的对象", "id" + data_file.get(indexs.get(i)).getObjectId());
            ImageVo temp = new ImageVo();
            temp.setObjectId(data_file.get(indexs.get(i)).getObjectId());
            selecturls.add(data_file.get(indexs.get(i)).getFile_url());
            selects.add(temp);
        }
        BmobUtil.getInstance().DeleteObjects(selects, new BaseBmobMultListener() {
            @Override
            public void onSuccess() {
                Logs.e("全清成功", "--需要删除文件");
                DeleteFiles(selecturls);
            }

            @Override
            public void onFailure(String errorcode, String errormsg) {
                Logs.e("删除出了一些问题" + errorcode, "---" + errormsg);
            }

            @Override
            public void onSuccess(List<Integer> errorcount) {
                Logs.e("部分出现了问题", "" + errorcount.toArray(new Integer[]{}).toString());

            }
        });
    }

    /**
     * 删除指定选中文件
     *
     * @param urls
     */
    private void DeleteFiles(List<String> urls) {
        String[] fileurls = urls.toArray(new String[urls.size()]);
        showPDialog();
        BmobUtil.getInstance().DeleteFiles(fileurls, new BmobFileListener() {
            @Override
            public void OnSuccess(String filepath) {
                hidePDialog();
                SuperToastUtil.getInstance(activity).showToast("删除文件成功");
                Refresh();
            }

            @Override
            public void OnFail(String code, String message) {
                hidePDialog();
                Logs.e("code" + code, message);
                SuperToastUtil.getInstance(activity).showToast("操作成功");
                //SuperToastUtil.getInstance(activity).showToast(code+message);
                Refresh();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            //单选
            //String result = data.getStringExtra(PhotoPickerActivity.SELECT_RESULTS);
            //多选
            List<String> results = data.getStringArrayListExtra(PhotoPickerActivity.SELECT_RESULTS_ARRAY);
            if (results == null) {
                SuperToastUtil.getInstance(activity).showToast("未选取内容~!");
                return;
            }
            UploadFiles(results);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传所选所有文件
     *
     * @param results
     */
    private void UploadFiles(List<String> results) {
        HashMap<String, String> params = new HashMap<>();
        params.put("groupid", groupid);
        params.put("userid", LoginUserEntity.userid);
        showPDialog();
        BmobUtil.getInstance().UploadFiles(new BmobMulitFileListener() {
            @Override
            public void OnSuccess(List<BmobFile> files, List<String> urls, int failcount) {
                Logs.e("完成上传", "准备更新数据");
                UpdataUserData(files, urls, failcount);
            }

            @Override
            public void OnProgress(int current, int total) {
                Logs.e("current" + current, "total" + total);
            }

            @Override
            public void OnFail(String code, String message) {
                hidePDialog();
                SuperToastUtil.getInstance(activity).showToast("上传失败" + message);
            }
        }, results.toArray(new String[]{}));
    }


    /**
     * 更新用户数据
     *
     * @param files
     * @param urls
     * @param failcount
     */
    private void UpdataUserData(List<BmobFile> files, List<String> urls, int failcount) {
        //   SuperToastUtil.getInstance(activity).showToast("上传完成"+str);
        showPDialog();
        List<BmobObject> addfile = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            ImageVo imageVo = new ImageVo(LoginUserEntity.userid, files.get(i).getUrl(), groupid);
            addfile.add(imageVo);
        }

        BmobUtil.getInstance().InsertBatch(false, addfile, new BaseBmobMultListener() {
            @Override
            public void onSuccess() {
                hidePDialog();
                SuperToastUtil.getInstance(activity).showToast("上传完成");
                Refresh();
            }

            @Override
            public void onFailure(String errorcode, String errormsg) {
                hidePDialog();
                SuperToastUtil.getInstance(activity).showToast("更新文档失败" + errormsg);
                Refresh();
            }

            @Override
            public void onSuccess(List<Integer> errorcount) {
                hidePDialog();
                SuperToastUtil.getInstance(activity).showToast("上传完成" + errorcount.toArray() + "上传出现问题");
                Refresh();
            }
        });
    }


    private void Refresh() {
        currentpage = 1;
        hasmore = true;
        data_file.clear();
        adapter.notifyDataSetChanged();

        if (data != null) data.clear();
        //rv_filedata.ResetStatue(true);
        adapter.SetSelectMode(false);
        SearchFile(true);
    }

    /**
     * 从网路中查找文件
     */
    private void SearchFile(final boolean isclear) {
        showPDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("groupid", groupid);
        params.put("userid", LoginUserEntity.userid);

        BmobUtil.getInstance().SearchDataByLimit(ImageVo.class, params, currentpage, new BmobQueryListener<ImageVo>() {
            @Override
            public void OnSuccess(ImageVo object) {

            }

            @Override
            public void OnSuccess(List<ImageVo> objects) {
                if (objects.size() > 0) {
                    currentpage++;
                    data_file.addAll(objects);
                    ConvertData(isclear,objects);
                } else {
                    hasmore = false;
                    hidePDialog();
                    SuperToastUtil.getInstance(activity).showToast("无更多内容!");
                }
            }

            @Override
            public void OnFail(String code, String error) {
                hidePDialog();
                Logs.e("code" + code, error);
                mHandler.sendEmptyMessage(1);
                SuperToastUtil.getInstance(activity).showToast("查找错误" + error);
            }
        });

    }

    /**
     * 转化数据
     *
     * @param objects
     */
    private void ConvertData(final boolean isclear, List<ImageVo> objects) {
        Observable.just(objects).map(new Func1<List<ImageVo>, ArrayList<String>>() {
            @Override
            public ArrayList<String> call(List<ImageVo> imageVos) {
                ArrayList<String> data = new ArrayList<String>();
                for (int i = 0; i < imageVos.size(); i++) {
                    data.add(imageVos.get(i).getFile_url());
                }
                return data;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ArrayList<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(ArrayList<String> strings) {
                       //     closeProgress();
                        hidePDialog();
                        if(isclear)
                            data.clear();
                        mHandler.sendEmptyMessage(1);
                        data.addAll(strings);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void initAction() {
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initRv() {


        data = new ArrayList<>();
        rv_filedata.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rv_filedata.setLaodingMoreProgressStyle(ProgressStyle.BallRotate);
        rv_filedata.setArrowImageView(R.drawable.iconfont_downgrey);
        rv_filedata.setPullRefreshEnabled(false);
        rv_filedata.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new FilesearchedAdapter(data, new FilesearchedAdapter.OnEventClick() {
            @Override
            public void onItemClick(View view, int position) {
                if (adapter.isSelectMode()) {
                    // adapter.notifyItemChanged(position-1,1);
                } else {
                    ShowPicDetail(position - 1);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!adapter.isSelectMode())
                    adapter.SetSelectMode(true);
                adapter.notifyItemChanged(position, 1);
                tv_right.setVisibility(adapter.isSelectMode() ? View.VISIBLE : View.INVISIBLE);
            }
        });

        rv_filedata.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessage(0);
                Refresh();
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        rv_filedata.loadMoreComplete();
                        if (hasmore) {
                            SearchFile(false);
                            rv_filedata.refreshComplete();
                        } else {
                            // 已无更多内容
                            adapter.notifyDataSetChanged();
                            rv_filedata.loadMoreComplete();
                        }
                    }
                }, 1000);
            }
        });

        rv_filedata.setAdapter(adapter);
    }

    public void ShowPicDetail(int position) {
        Intent intent = new Intent(activity, ImgDetailActivity.class);
        intent.putStringArrayListExtra("images", data);
        intent.putExtra("download", "true");
        intent.putExtra("point", position);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {

        if (adapter.isSelectMode()) {
            adapter.SetSelectMode(false);
            tv_right.setVisibility(View.GONE);
        } else
            super.onBackPressed();
    }

    private void initAnimationSet() {
        animationSet = new AnimationSet(true);
        final RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(2000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        final RotateAnimation animation1 = new RotateAnimation(0f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation1.setDuration(2000);
        animation1.setInterpolator(new LinearInterpolator());
        animation1.setRepeatCount(Animation.INFINITE);
        animation1.setRepeatMode(Animation.RESTART);
        animationSet.addAnimation(animation);
        animationSet.addAnimation(animation1);
        animationSet.setRepeatMode(Animation.RESTART);
        animationSet.setRepeatCount(Animation.INFINITE);

        closeanimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f);
        closeanimation.setDuration(800);
        closeanimation.setInterpolator(new FastOutSlowInInterpolator());
        closeanimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_progress.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 当
     */
    private void showProgress() {
        if (img_progress != null) {
            img_progress.setVisibility(View.VISIBLE);
            //TranslateAnimation animation=new TranslateAnimation(0,20,0,20);
            img_progress.startAnimation(animationSet);
        }
    }

    private void closeProgress() {
        if (img_progress != null) {
            img_progress.clearAnimation();
            img_progress.setAnimation(closeanimation);
        }
    }
}
