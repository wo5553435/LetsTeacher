package com.example.sinner.letsteacher.interfaces.behavior;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;

import com.example.sinner.letsteacher.utils.AnimatorUtil;
import com.pigcms.library.utils.Logs;

/**
 * Created by sinner on 2017-07-10.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class ShowwithoutActionBehavior extends FloatingActionButton.Behavior {
    Context context;
    private boolean isScrolling=false;
    public ShowwithoutActionBehavior(Context context, AttributeSet attrs) {
        super();
        this.context=context;
    }

    //开始滑动
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, final FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
//        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(child.getVisibility()==View.VISIBLE){
                    Logs.e("开始关闭","---");
                    AnimatorUtil.scaleHide(child, viewPropertyAnimatorListener);
                }
            }
        },100);
        Logs.e("onStartNestedScroll","-------");
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }



    //滑动中
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Logs.e("onNestedScroll","-------");
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        isScrolling=true;
    }

    //停止滑动

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, final FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        Logs.e("onStopNestedScroll","-------");
        isScrolling=false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(child.getVisibility()==View.INVISIBLE){
                    if(!isScrolling)
                        Logs.e("开始展现","---");
                        AnimatorUtil.scaleShow(child, null);//
                    }
            }
        },700);


    }


    boolean isAnimatingOut = false;

    public ViewPropertyAnimatorListener viewPropertyAnimatorListener = new ViewPropertyAnimatorListener() {

        @Override
        public void onAnimationStart(View view) {
            isAnimatingOut = true;
        }

        @Override
        public void onAnimationEnd(View view) {
            isAnimatingOut = false;
            Logs.e("动画结束隐藏","---");
            view.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationCancel(View arg0) {
            isAnimatingOut = false;
        }
    };

}
