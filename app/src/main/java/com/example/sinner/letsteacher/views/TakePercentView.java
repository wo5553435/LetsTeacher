package com.example.sinner.letsteacher.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sinner on 2017-06-28.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class TakePercentView extends View implements View.OnTouchListener{
    public TakePercentView(Context context) {
        super(context);
    }

    public TakePercentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TakePercentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
