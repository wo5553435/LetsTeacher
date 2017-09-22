package com.example.sinner.letsteacher.interfaces.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.example.sinner.letsteacher.R;


/**
 * Created by win7 on 2017-03-03.
 */

public class TopBehavior extends CoordinatorLayout.Behavior {
    private int targetId;//自定义标签中保存的值 （这里xml需要保存的是一个id）

    public TopBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Follow);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if(a.getIndex(i) == R.styleable.Follow_target){
                targetId = a.getResourceId(attr, -1);
            }
        }
        a.recycle();
    }





   /* @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
          return dependency.getId() == R.id.first;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }*/

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setY(dependency.getY()+dependency.getHeight());//将位置保留dependency的高度缺省值
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency.getId() == targetId;
    }
}
