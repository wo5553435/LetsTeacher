package com.example.sinner.letsteacher.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.adapter.TestAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView rv;

    private Unbinder unbinder;
    @BindView(R.id.sw_test)
    SwipeRefreshLayout swipeRefreshLayout;

    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        unbinder=ButterKnife.bind(this);
        //mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.tab_layout));
        initRv();
    }

    private void initRv() {
        rv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        //设置item之间的间隔
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        rv.addItemDecoration(decoration);
        rv.setAdapter(new TestAdapter(true, this, new TestAdapter.OnEventClick() {
            @Override
            public void onItemClick(View view, int position) {
               startActivity(new Intent(Main2Activity.this,Main3Activity.class));
                /*if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }*/
            }
        }));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1500);
            }
        });

        AccelerateDecelerateInterpolator interpolator=new AccelerateDecelerateInterpolator();
        for (int i = 0; i < 100; i++) {
            Log.e("i---"+i,""+interpolator.getInterpolation(i/100f));

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
