package com.example.sinner.letsteacher.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.adapter.FilesearchedAdapter;
import com.example.sinner.letsteacher.utils.file.FileUtils;
import com.pigcms.library.utils.Logs;
import com.pigcms.library.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by sinner on 2017-06-29.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class SearchFileActivity extends BasicActivity {
    @BindView(R.id.rv_searchfile_data)
    RecyclerView rv_filedata;

    @BindView(R.id.img_search_progress)
    AppCompatImageView img_progress;

    @BindView(R.id.title_righttext)
    TextView tv_right;

    @BindView(R.id.title_lin_back)
    LinearLayout layout_back;

    AnimationSet animationSet;
    Animation closeanimation;

    public ArrayList<String> Allfilepaths = new ArrayList<>();

    FilesearchedAdapter adapter;
    @Override
    protected int getContentLayout() {
        return R.layout.layout_activity_searchfile;
    }

    @Override
    protected void initGui() {

    }

    @Override
    protected void initData() {
        initAnimationSet();
        initRv();
        SearchFile();
    }



    private void initRv() {
        rv_filedata.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        adapter= new FilesearchedAdapter(Allfilepaths, new FilesearchedAdapter.OnEventClick() {
            @Override
            public void onItemClick(View view, int position) {
                if(adapter.isSelectMode()){
                    adapter.notifyItemChanged(position,1);
                }else{
                    ShowPicDetail(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if(!adapter.isSelectMode())
                    adapter.SetSelectMode(true);
                    adapter.notifyItemChanged(position,1);
            }
        });

        rv_filedata.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if(adapter.isSelectMode()){
            adapter.SetSelectMode(false);
        }else{
            super.onBackPressed();
        }
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

    @Override
    protected void initAction() {

    }

    private void SearchFile() {
        Allfilepaths.clear();
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                FileUtils.getInstance().setListener(new FileUtils.searchFileListener() {
                    @Override
                    public void searchone(String filpath) {
                        subscriber.onNext(filpath);
                    }

                    @Override
                    public void searchover() {
                        subscriber.onCompleted();
                    }
                }).GetFiles(path, "png", "jpg");
            }
        }).buffer(3)//一次性最多捆绑3个 这样影响查找速率   但是总比背压好 不影响GPu
         .zipWith(Observable.interval(200, TimeUnit.MILLISECONDS), new Func2<List<String>, Long, List<String>>() {//上游
             @Override
             public List<String> call(List<String> strings, Long aLong) {
                 return strings;
             }
         })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgress();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    Subscriber subscriber=new Subscriber<List<String>>() {
        @Override
        public void onCompleted() {//完成搜索后 在此刷新整个rv
            closeProgress();
        }

        @Override
        public void onError(Throwable throwable) {
            Logs.e("-onError-", "" + throwable.getMessage());
        }

        @Override
        public void onNext(List<String> strings) {
            if (strings != null) {//衔接过渡动画 ，让用户能感觉在查找
                Allfilepaths.addAll(strings);
                adapter.notifyItemChanged(Allfilepaths.size()-strings.size());
                Log.e("item size", ""+ adapter.getItemCount());
            }
        }
    };

    public void ShowPicDetail(int position){
        Intent intent=new Intent(activity,ImgDetailActivity.class);
        intent.putStringArrayListExtra("images",Allfilepaths);
        intent.putExtra("point",position);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriber.unsubscribe();

    }

    /**
     * 当
     */
    private void showProgress() {
        img_progress.setVisibility(View.VISIBLE);
        //TranslateAnimation animation=new TranslateAnimation(0,20,0,20);
        img_progress.startAnimation(animationSet);
    }

    private void closeProgress() {
        img_progress.clearAnimation();
        img_progress.setAnimation(closeanimation);

    }
}
