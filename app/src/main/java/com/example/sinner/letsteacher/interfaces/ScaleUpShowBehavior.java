package com.example.sinner.letsteacher.interfaces;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;

import com.example.sinner.letsteacher.utils.AnimatorUtil;
import com.example.sinner.letsteacher.utils.Logs;

/**
 * Created by win7 on 2017-03-01.
 */

public class ScaleUpShowBehavior extends FloatingActionButton.Behavior {


    public ScaleUpShowBehavior(Context context, AttributeSet attrs) {
        super();
    }

    //开始滑动
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
//        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }


    //滑动中
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
       //

       // Logs.e("onNestedScroll--1","dxConsumed:"+dxConsumed+"  dyConsumed:"+dyConsumed+"   dxUnconsumed:"+dxUnconsumed+"    dyUnconsumed:"+dyUnconsumed);
       // if (dyConsumed > 0 && dyUnconsumed == 0) {
//            System.out.println("上滑中。。。");
//        }
//        if (dyConsumed == 0 && dyUnconsumed > 0) {
//            System.out.println("到边界了还在上滑。。。");
//        }
//        if (dyConsumed < 0 && dyUnconsumed == 0) {
//            System.out.println("下滑中。。。");
//        }
//        if (dyConsumed == 0 && dyUnconsumed < 0) {
//            System.out.println("到边界了，还在下滑。。。");
//        }
        ///    Logs.e("onNestedScroll--2","dyConsumed:"+dyConsumed+"  dyUnconsumed:"+dyUnconsumed+"   isAnimatingOut:"+isAnimatingOut+"    child.getVisibility():"+child.getVisibility());

            if ((dyConsumed > 0 || dyUnconsumed > 0) && !isAnimatingOut
                    && child.getVisibility() == View.VISIBLE) {// 手指上滑，隐藏FAB
                AnimatorUtil.scaleHide(child, viewPropertyAnimatorListener);
            } else if ((dyConsumed < 0 || dyUnconsumed < 0) && child.getVisibility() != View.VISIBLE) {
                AnimatorUtil.scaleShow(child, null);// 手指下滑，显示FAB
            }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
     //   }
    }

    //停止滑动

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }
    boolean isAnimatingOut = false;

    ViewPropertyAnimatorListener viewPropertyAnimatorListener = new ViewPropertyAnimatorListener() {

        @Override
        public void onAnimationStart(View view) {
            isAnimatingOut = true;
        }

        @Override
        public void onAnimationEnd(View view) {
            isAnimatingOut = false;
            view.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationCancel(View arg0) {
            isAnimatingOut = false;
        }
    };


}
