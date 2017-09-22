package com.example.sinner.letsteacher.interfaces.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by win7 on 2017-03-02.
 */

public class BasicBehavior<T extends View> extends CoordinatorLayout.Behavior<T> {
    public BasicBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
