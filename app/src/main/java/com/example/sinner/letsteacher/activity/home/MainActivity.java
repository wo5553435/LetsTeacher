package com.example.sinner.letsteacher.activity.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.activity.SpacesItemDecoration;
import com.example.sinner.letsteacher.activity.TableActivity;
import com.example.sinner.letsteacher.adapter.TestAdapter;
import com.example.sinner.letsteacher.utils.AnimatorUtil;
import com.example.sinner.letsteacher.utils.file.FileUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by win7 on 2017-02-22.
 */

public class MainActivity  extends AppCompatActivity{

    private Unbinder unbinder;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    private FloatingActionButton FAB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_main);
        unbinder= ButterKnife.bind(this);
        initView();
        initAction();
        initData();
    }

    private void initView() {
        FAB= (FloatingActionButton) findViewById(R.id.fab);
    }

    private void initAction() {
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path=Environment.getExternalStorageDirectory().getAbsolutePath();
                FileUtils.getInstance().GetFiles(path,"png","jpg");
            }
        });
    }

    private void initData() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle("首页");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色

//设置收缩后Toolbar上字体的颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        //设置item之间的间隔
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(new TestAdapter(true, this, new TestAdapter.OnEventClick() {
            @Override
            public void onItemClick(View view, int position) {
                if(position==0)
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                else{
                    startActivity(new Intent(MainActivity.this,TableActivity.class));
                }
            }
        }));
    }
    private boolean isInitializeFAB = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isInitializeFAB) {
            isInitializeFAB = true;
          //  hideFAB();
        }
    }




        private void hideFAB() {
            FAB.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AnimatorUtil.scaleHide(FAB, new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                        }
                        @Override
                        public void onAnimationEnd(View view) {
                            FAB.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onAnimationCancel(View view) {
                        }
                    });
                }
            }, 500);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
