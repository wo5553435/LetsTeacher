package com.example.sinner.letsteacher.interfaces.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.sinner.letsteacher.utils.AnimatorUtil;


/**
 * Created by win7 on 2017-03-02.
 */

public class ScaleBehavior extends  BasicBehavior<View> {
    private ListenerAnimatorEndBuild listenerAnimatorEndBuild;

    public ScaleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        listenerAnimatorEndBuild = new ListenerAnimatorEndBuild();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        if (dyConsumed > 0 && dyUnconsumed == 0) {
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

        // 这里可以写你的其他逻辑动画，这里只是举例子写了个缩放动画。
        if ((dyConsumed > 0 || dyUnconsumed > 0) && listenerAnimatorEndBuild.isFinish() && child.getVisibility() == View.VISIBLE) {//往下滑
            AnimatorUtil.transHide(child, listenerAnimatorEndBuild.build());
        } else if ((dyConsumed < 0 || dyUnconsumed < 0) && child.getVisibility() != View.VISIBLE) {
            AnimatorUtil.transShow(child, null);
        }
    }
}
