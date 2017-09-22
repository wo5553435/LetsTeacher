package com.example.sinner.letsteacher.utils;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by win7 on 2017-03-01.
 */

public class AnimatorUtil {

    // 显示view
    public static void scaleShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(2500)
                .setListener(viewPropertyAnimatorListener)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }


    // 显示view
    public static void scaleShow(View view, long time,ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(time)
                .setListener(viewPropertyAnimatorListener)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }


    // 隐藏view
    public static void scaleHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        ViewCompat.animate(view)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .alpha(0.0f)
                .setDuration(500)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setListener(viewPropertyAnimatorListener)
                .start();
    }


    // 隐藏view
    public static void scaleHide(View view,long time, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        ViewCompat.animate(view)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .alpha(0.0f)
                .setDuration(time)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setListener(viewPropertyAnimatorListener)
                .start();
    }

    public static void transHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        ViewCompat.animate(view)
                .scaleX(1.0f)
                .scaleY(0.0f)
                .alpha(1.0f)
                .setDuration(500)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setListener(viewPropertyAnimatorListener)
                .start();
    }

    // 显示view
    public static void transShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(500)
                .setListener(viewPropertyAnimatorListener)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }
}
