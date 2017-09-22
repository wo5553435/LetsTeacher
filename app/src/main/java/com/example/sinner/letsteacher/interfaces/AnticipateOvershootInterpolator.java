/*
package com.example.sinner.letsteacher.interfaces;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

*/
/**
 * Created by win7 on 2017-06-17.
 *//*

public class AnticipateOvershootInterpolator implements Interpolator {
    private final float mTension;

    public AnticipateOvershootInterpolator() {
        mTension = 2.0f * 1.5f;
    }

    */
/**
     * @param tension Amount of anticipation/overshoot. When tension equals 0.0f,
     *                there is no anticipation/overshoot and the interpolator becomes
     *                a simple acceleration/deceleration interpolator.
     *//*

    public AnticipateOvershootInterpolator(float tension) {
        mTension = tension * 1.5f;
    }

    */
/**
     * @param tension Amount of anticipation/overshoot. When tension equals 0.0f,
     *                there is no anticipation/overshoot and the interpolator becomes
     *                a simple acceleration/deceleration interpolator.
     * @param extraTension Amount by which to multiply the tension. For instance,
     *                     to get the same overshoot as an OvershootInterpolator with
     *                     a tension of 2.0f, you would use an extraTension of 1.5f.
     *//*

    public AnticipateOvershootInterpolator(float tension, float extraTension) {
        mTension = tension * extraTension;
    }

    public AnticipateOvershootInterpolator(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, AnticipateOvershootInterpolator);

        mTension = a.getFloat(AnticipateOvershootInterpolator_tension, 2.0f) *
                a.getFloat(AnticipateOvershootInterpolator_extraTension, 1.5f);

        a.recycle();
    }

    private static float a(float t, float s) {
        return t * t * ((s + 1) * t - s);
    }

    private static float o(float t, float s) {
        return t * t * ((s + 1) * t + s);
    }

    public float getInterpolation(float t) {
        // a(t, s) = t * t * ((s + 1) * t - s)
        // o(t, s) = t * t * ((s + 1) * t + s)
        // f(t) = 0.5 * a(t * 2, tension * extraTension), when t < 0.5
        // f(t) = 0.5 * (o(t * 2 - 2, tension * extraTension) + 2), when t <= 1.0
        if (t < 0.5f) return 0.5f * a(t * 2.0f, mTension);
        else return 0.5f * (o(t * 2.0f - 2.0f, mTension) + 2.0f);
    }
}*/
