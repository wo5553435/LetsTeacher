package com.example.sinner.letsteacher.interfaces.behavior;

import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;

/**
 * Created by win7 on 2017-03-02.
 * behavior动画监听类
 */

public   class ListenerAnimatorEndBuild {
    // 记录View移出动画是否执行完。
    private static boolean isOutExecute = false;

    private static ViewPropertyAnimatorListener outAnimatorListener;

    public ListenerAnimatorEndBuild() {
        outAnimatorListener = new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                isOutExecute = true;
            }
            @Override
            public void onAnimationEnd(View view) {
                view.setVisibility(View.GONE);
                isOutExecute = false;
            }

            @Override
            public void onAnimationCancel(View view) {
                isOutExecute = false;
            }
        };
    }

    // View移出动画是否执行完。
    public static boolean isFinish() {
        return !isOutExecute;
    }

    // 返回ViewPropertyAnimatorListener。
    public  static ViewPropertyAnimatorListener build() {
        return outAnimatorListener;
    }
}
