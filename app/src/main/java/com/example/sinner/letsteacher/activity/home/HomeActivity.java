package com.example.sinner.letsteacher.activity.home;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.activity.AdviceActivity;
import com.example.sinner.letsteacher.activity.BasicActivity;
import com.example.sinner.letsteacher.activity.SearchFileActivity;
import com.example.sinner.letsteacher.activity.TinkerActivity;
import com.example.sinner.letsteacher.activity.camera.CameraActivity;
import com.example.sinner.letsteacher.activity.clearboom.ClearBoomActivity;
import com.example.sinner.letsteacher.activity.login.LoginAc;
import com.example.sinner.letsteacher.fragments.HomeFragment;
import com.example.sinner.letsteacher.utils.SuperToastUtil;
import com.github.johnpersano.supertoasts.SuperToast;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by win7 on 2017-06-13.
 */

public class HomeActivity extends BasicActivity {
    @BindView(R.id.tl_custom)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.tv_toolbar_name)
    TextView tv_toolbar_name;

    @BindView(R.id.layout_minefragment_shopinfo)
    RelativeLayout layout_myinfo;

    @BindView(R.id.layout_minefragment_signout)
    RelativeLayout layout_signout;

    @BindView(R.id.layout_minefragment_advice)
    RelativeLayout layout_advice;

    @BindView(R.id.profile_image_menu)
    CircleImageView img;

    @BindView(R.id.layout_minefragment_search)
    RelativeLayout layout_search;


    HomeFragment homePageFragment;
    ActionBarDrawerToggle mDrawerToggle;

    @BindView(R.id.img_menu_icon)
    AppCompatImageView icon_menu;
    AnimatedVectorDrawable anim1;


    @Override
    protected int getContentLayout() {
        return R.layout.layout_activity_home;
    }

    @Override
    protected void initGui() {
        anim1 = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.animatieimg_search);
        icon_menu.setImageDrawable(anim1);
    }

    @Override
    protected void initData() {
        initShareView();
        initTitle();
        initFragment();
    }

    private void initShareView() {
//        postponeEnterTransition();
//
//        final View decor = getWindow().getDecorView();
//        decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                decor.getViewTreeObserver().removeOnPreDrawListener(this);
//                startPostponedEnterTransition();
//                return true;
//            }
//        });


    }

    @Override
    protected void initAction() {
        icon_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Drawable drawable = ((AppCompatImageView)view).getDrawable();
//                if (drawable instanceof Animatable) {
//                    ((Animatable) drawable).start();
//                }
                anim1.start();

                /*ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(icon_menu, "translationX", 1f, 1.5f);
                scaleXAnimator.setDuration(500);
                scaleXAnimator.setRepeatCount(1);
                scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
                scaleXAnimator.start();*/
//                ViewCompat.animate(icon_menu).translationX(100).setDuration(200).start();
            }
        });

        layout_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }
        });
        layout_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signout();
            }
        });
        layout_advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoAdvice();
            }


        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity,
                      ClearBoomActivity.class
                       // TinkerActivity.class
                       // CameraActivity.class
                ));
            }
        });
    }

    @OnClick({R.id.layout_minefragment_search})
    public void OnClickEvent(View view){
        switch (view.getId()){
            case R.id.layout_minefragment_search:
                if(!isFastDoubleClick()) startActivity(new Intent(activity,SearchFileActivity.class));
                break;
        }
    }

    private void GotoAdvice() {
        startActivity(new Intent(activity, AdviceActivity.class));
    }

    /**
     * 退出登录操作
     */
    private void Signout() {
        Intent intent=new Intent(activity,LoginAc.class);
        intent.putExtra("clear","--");
        startActivity(intent);
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ViewCompat.animate(tv_toolbar_name).setInterpolator(new FastOutLinearInInterpolator()).setDuration(1200).alpha(0).start();
                }
            },1000);
        }
    }

    protected  void initTitle(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset){
                super.onDrawerSlide(drawerView,slideOffset);
                tv_toolbar_name.setAlpha(slideOffset);
                toolbar.getBackground().mutate().setAlpha((int) (255*(slideOffset)));

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


    private void initFragment() {
        homePageFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.content_main2, homePageFragment);
        transaction.commit();
        //511477460365 363424879610
        // SearchCode("");
    }

    private void ShowDialog(){
//        ColorSelectFragment colorfragment = new ColorSelectFragment();
//        colorfragment.show(getSupportFragmentManager(), "dialog");
//        String path= Environment.getExternalStorageDirectory().getAbsolutePath();
//        List<String> pngs=FileUtils.getInstance().GetFiles(path,true,"png");
//        Logs.e("pngs file size is",""+pngs.size());
    }

    long lasttime=0;
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        long currenttime=System.currentTimeMillis();
        if(currenttime-lasttime<=1500){
            SuperToast.cancelAllSuperToasts();
            finish();
        }else{
            lasttime=currenttime;
            SuperToastUtil.getInstance(activity).showToast("双击后退退出应用~");
        }
    }
}
