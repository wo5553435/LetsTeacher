package com.example.sinner.letsteacher.fragments;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.activity.AddNewFileMkdirs;
import com.example.sinner.letsteacher.activity.ShowNetPicActivity;
import com.example.sinner.letsteacher.activity.TestActivity;
import com.example.sinner.letsteacher.adapter.FileListAdapter;
import com.example.sinner.letsteacher.entity.FileBean;
import com.example.sinner.letsteacher.entity.ImageVo;
import com.example.sinner.letsteacher.entity.LoginUserEntity;
import com.example.sinner.letsteacher.utils.AndroidUtils;
import com.example.sinner.letsteacher.utils.AnimatorUtil;
import com.example.sinner.letsteacher.utils.Logs;
import com.example.sinner.letsteacher.utils.StringTools;
import com.example.sinner.letsteacher.utils.SuperToastUtil;
import com.example.sinner.letsteacher.utils.bmob.BmobUtil;
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobAddOrUpdateListener;
import com.example.sinner.letsteacher.utils.bmob.listener.data.BmobQueryListener;
import com.example.sinner.letsteacher.utils.file.FileUtils;
import com.example.sinner.letsteacher.views.dialog.TextDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by win7 on 2017-06-14.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    boolean isDebug = true;
    private ImageView img_test;

    @BindView(R.id.rv_list_home)
    RecyclerView rv_list;

    @BindView(R.id.home_sceneroot)
    FrameLayout layut_root;

    private List<FileBean> data_files;
    FileListAdapter adapter;
    //    @BindView(R.id.tv_test_test)
    TextView tv_groupname, tv_groupback, tv_actions, tv_tip1, tv_tip2, tv_tip3;
    ImageView tv_tip4;
    @BindView(R.id.fb_home_save)
    FloatingActionButton fab;

    View specialView;

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getMianLayout() {
        return R.layout.layout_fragment_homepage;
    }

    @Override
    public void initView() {
        img_test = (ImageView) mainView.findViewById(R.id.img_test);
//        tv_test= (TextView) mainView.findViewById(R.id.home_sceneroot);
    }

    @Override
    public void initAction() {
        img_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                VectorDrawableCompat drawableCompat;
//                Drawable drawable = ((AppCompatImageView)view).getDrawable();
//                if (drawable instanceof Animatable) {
//                    ((Animatable) drawable).start();
//                }
                // ViewCompat.animate(img_test).rotationYBy(90).rotationY(360).setDuration(1000).start();
                AnimationSet animationSet = new AnimationSet(true);
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
                //TranslateAnimation animation=new TranslateAnimation(0,20,0,20);
                img_test.startAnimation(animationSet);
            }
        });
    }

    @Override
    public void initData() {
        initRv();
        changeLayoutToAdd();
    }

    /**
     * 初始化rv
     */
    private void initRv() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity, 4);
        layoutManager.setAutoMeasureEnabled(true);
        rv_list.setLayoutManager(layoutManager);
        data_files = new ArrayList<>();
        adapter = new FileListAdapter(data_files, new FileListAdapter.OnEventClick() {
            @Override
            public void onItemClick(final View view, final int position) {

                Intent intent;
                if (position == data_files.size()) {//添加页面
//                    intent=new Intent(activity, AddNewFileMkdirs.class);
//                    startActivityForResult(intent,3/*,ac.toBundle()*/);
                    if (specialView == null) specialView = view;
                    if (currentselect != -1)
                        layut_root.findViewById(R.id.root).setVisibility(View.VISIBLE);
                    if (specialView == null) specialView = view;
                    switchScene(specialView, !isScene);
                    // startActivity(new Intent(activity, TestActivity.class));
//                    ShapeFragment fragment=new ShapeFragment();
//                    fragment.show(getActivity().getSupportFragmentManager(), "searchdialog");
                } else {//打开操作
                    currentselect = position;
                    if (isScene && specialView != null) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gotoDetail(position, view);
                            }
                        }, 500);
                        switchScene(specialView, !isScene);
                    } else {
                        gotoDetail(position, view);
                    }
                }
            }
        });
        rv_list.setAdapter(adapter);
        GeFileList();
    }


    /**
     * 转换布局
     */
    private void changeLayoutToAdd() {
        ViewGroup sceneRoot = layut_root;
        scene1 = Scene.getSceneForLayout(sceneRoot, R.layout.layout_frame_sign, activity);
        scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.layout_frame_nosign, activity);
        TransitionManager.go(scene2);
        //FindAllInfoView();
    }

    /**
     * 查找填充信息布局控件
     */
    private void FindAllInfoView() {
//        tv_groupname= (TextView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_name);
//        tv_grouptime= (TextView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_ctime);
//        tv_actions= (TextView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_action);
//        tv_tip1= (TextView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_tip);
//        tv_tip2= (TextView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_tip1);
//        tv_tip3= (TextView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_tip2);
//        tv_tip4=(ImageView) scene2.getSceneRoot().findViewById(R.id.tv_groupinfo_tip3);
//
//
//        tv_actions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    private void ShowOrHide(boolean isshow) {
        tv_tip4.setVisibility(isshow ? View.VISIBLE : View.INVISIBLE);
    }

    int currentselect = -1;

    /**
     * 展示文件夹操作选项
     */
    private void showActions() {
    }

    private Scene scene1, scene2;
    private boolean isScene = false;
    private Transition transition;
    Animator circularReveal, endcularReveal;

    private void switchScene(View view, boolean flag) {
        if (transition == null) {
            Slide slide = new Slide();
            slide.setDuration(500);
            slide.setSlideEdge(Gravity.LEFT);
            Fade fade = new Fade();
            fade.setDuration(500);
            transition = new TransitionSet().addTransition(slide);
        }


        if (flag) {
            initStartAnimation();
            circularReveal.start();
            float x = 0, y = 0;
            int sunwidth = rv_list.getMeasuredWidth();
            x = sunwidth - view.getX() - view.getMeasuredWidth() + 30;
            y = view.getHeight() / 2 - 20;
            Logs.e("x" + view.getX(), "y" + view.getY());
            Logs.e("sx" + sunwidth + "---x" + x, "sy" + y);
            Logs.e("w" + view.getMeasuredWidth(), "h" + view.getMeasuredHeight());
            ViewCompat.animate(view).rotation(135f).scaleX(0.5f).scaleY(0.5f).setDuration(700).setInterpolator(new AnticipateOvershootInterpolator()).translationX(x).translationY(y).start();
        } else {
            initEndAnimation();
            endcularReveal.start();
            ViewCompat.animate(view).rotation(0f).scaleX(1.0f).scaleY(1.0f).setDuration(700).setInterpolator(new AnticipateOvershootInterpolator()).translationX(0).translationY(0).start();
        }
        isScene = !isScene;
        TransitionManager.go(flag ? scene1 : scene2, transition);
        if (flag) {
            tv_groupname = (TextView) scene1.getSceneRoot().findViewById(R.id.ed_add_gruopname);
            tv_groupback = (TextView) scene1.getSceneRoot().findViewById(R.id.ed_add_gruopback);
            tv_groupname.setOnClickListener(this);
            tv_groupback.setOnClickListener(this);
        }
    }


    @OnClick({R.id.fb_home_save})
    public void OnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.fb_home_save:
                CheckCommit();
                break;
        }
    }

    /**
     * 展示
     */
//    private void showGroupDetail(int position,View view){
//        FileBean data=data_files.get(position);
//        tv_groupname.setText(data.getName());
//        tv_grouptime.setText(data.getCreatedAt());
//    }
    private void gotoDetail(int position, View view) {
        Intent intent;
        intent = new Intent(activity, ShowNetPicActivity.class);
        intent.putExtra("name", data_files.get(position).getName());
        intent.putExtra("id", data_files.get(position).getGroupid());
        ActivityOptionsCompat ac = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, new Pair<View, String>(view.findViewById(R.id.tv_flieitem_name), "title"));
        startActivityForResult(intent, 2, ac.toBundle());
    }

    private void initEndAnimation() {
        endcularReveal = ViewAnimationUtils.createCircularReveal(layut_root, 0, 0
                , 1.5f * Math.max(layut_root.getWidth(), layut_root.getHeight()), 0);
        endcularReveal.setDuration(500);
        endcularReveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                layut_root.setBackgroundColor(getResources().getColor(R.color.menu_blue));
                AnimatorUtil.scaleHide(fab, 500, new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        fab.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                });
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                layut_root.setBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    int i = 0;

    private void initStartAnimation() {
        circularReveal = ViewAnimationUtils.createCircularReveal(layut_root, 0, 0
                , 0, 1.5f * Math.max(layut_root.getWidth(), layut_root.getHeight()));
        circularReveal.setDuration(500);
        circularReveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                AnimatorUtil.scaleShow(fab, 500, new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        fab.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(View view) {

                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                });
                //layut_root.setBackgroundColor(getResources().getColor(R.color.menu_blue));
//                tv_test= (TextView) layut_root.findViewById(R.id.tv_test_test);
//                if(tv_test!=null)
//                    tv_test.setText("text"+i);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:
//                data_files.clear();
//                GeFileList();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void GeFileList() {

        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("userid", "");
        BmobUtil.getInstance().SearchData(FileBean.class, null, new BmobQueryListener<FileBean>() {

            @Override
            public void OnSuccess(FileBean object) {

            }

            @Override
            public void OnSuccess(List<FileBean> objects) {
                Logs.e("objects", "---" + objects.size());
                for (int i = 0; i < objects.size(); i++) {
                    if (objects.get(i) != null) {
                        data_files.add(objects.get(i));
                    }
                }
                hideProgressDialog();
//                data_files.add(new FileBean());
                adapter.notifyItemRangeChanged(0, data_files.size());
            }

            @Override
            public void OnFail(String code, String error) {
                hideProgressDialog();
                Logs.e("查询出错--" + code, error);
            }
        });
    }

    /**
     * 检查是否超过文件夹最大限制 8个
     */
    private void CheckCount() {
        FileBean temp = new FileBean();
//        temp.setUserid(LoginUserEntity.userid);
        Map<String, Object> limit = new HashMap<>();
        limit.put("userid", LoginUserEntity.userid);
        showProgressDialog();
        BmobUtil.getInstance().CountData(temp, limit, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    if (integer < 8) {
                        addGroup();
                    } else {
                        hideProgressDialog();
                        SuperToastUtil.getInstance(activity).showToast("抱歉!至多只能创建8个目录");
                    }
                } else {
                    hideProgressDialog();
                    Logs.e("" + e.getErrorCode(), "" + e.getMessage());
                    SuperToastUtil.getInstance(activity).showToast("抱歉,提交出现了问题！" + e.getErrorCode());
                }
            }
        });
    }


    private void addGroup() {
        FileBean bean = new FileBean();
        Logs.e("---id", "" + bean.getObjectId());
        bean.setName(tv_groupname.getText().toString());
        bean.setRemark(tv_groupback.getText().toString());
        bean.setColorids(AndroidUtils.Companion.getRandomColor());
        bean.setGroupid("" + System.currentTimeMillis());
        bean.setUserid(LoginUserEntity.userid);
//        bean.setGroupid(bean.getObjectId());
        bean.setDelete(false);
        showProgressDialog();
        BmobUtil.getInstance().addData(bean, new BmobAddOrUpdateListener() {
            @Override
            public void OnSuccess(String backstr) {
                hideProgressDialog();
                SuperToastUtil.getInstance(activity).showToast("保存成功！");
                if (isScene)
                    switchScene(specialView, false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Refresh();
                    }
                }, 400);
            }

            @Override
            public void OnFail(String errormsg) {
                hideProgressDialog();
                SuperToastUtil.getInstance(activity).showToast("提交失败!" + errormsg);
            }
        });
    }

    /**
     * 更新接口
     */
    private void Refresh() {
        specialView = null;
        initRv();
    }


    @Override
    public void clear() {

    }

    @Override
    public void onClick(final View view) {
        TextDialog dialog = new TextDialog(activity, R.style.DialogStyle);
        dialog.setText(((TextView) view).getText().toString());
        dialog.setListener(new TextDialog.OnResultListener() {
            @Override
            public void Done(String str) {
                ((TextView) view).setText(str);
            }
        });
        dialog.show();
    }

    private void CheckCommit() {
        String name = tv_groupname.getText().toString();
        String remark = tv_groupback.getText().toString();
        Logs.e("name", "" + name);
        Logs.e("back", "" + remark);
        if (StringTools.INSTANCE.getNotNullStr(name).length() != 0/*&&StringTools.getNotNullStr(remark).length()!=0*/) {
            CheckCount();
        } else {

        }
    }
}
