package com.example.sinner.letsteacher.interfaces.behavior;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.example.sinner.letsteacher.utils.Logs;


/**
 * Created by win7 on 2017-03-03.
 */

public class ScrollToTopBehavior extends CoordinatorLayout.Behavior<View>{int offsetTotal = 0;
    int offset = 20;
    boolean scrolling = false;
    CoordinatorLayout.LayoutParams params;
    private int viewheight=-1;
    private View  mContainer;
    AccelerateDecelerateInterpolator interpolator=new AccelerateDecelerateInterpolator();

    public ScrollToTopBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.e("onStartNestedScroll","----");
        return true;
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        Log.e("onNestedFling","----");
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        offset(child, dyConsumed,dyUnconsumed);
        Log.e("onNestedScroll","----");
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        Log.e("onStopNestedScroll","----");
        if(!scrolling)
        InterceptView(child);
    }


    public void offset(View child, int dy, int udy){
        if(params==null ) {
            params= (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            viewheight=params.topMargin;
            mContainer=child;
        }
        if(scrolling)
            return;
        //这里
        scrolling = true;
        View childs=child;
        if(udy==0){//正在滑动中
            if(dy>=0&&offset>0){//上拉
                if(offset>0){
                    offset=offset-2;
                    params.height= (int) (viewheight*((offset)/20f));
                    childs.setLayoutParams(params);
                }
            }else if(dy<=0&&offset<20){//下拉
                if(offset<20){
                    offset=offset+2;
                    params.height= (int) (viewheight*(offset/20f));
                    //params.topMargin= (int) (viewheight*((offset)/20f));
                    childs.setLayoutParams(params);
                }
            }
        }else if(udy<0){//到顶下拉
            params.height=viewheight;
            childs.setLayoutParams(params);
        }else if(udy>0){//到底上拉
            params.height=0;
            childs.setLayoutParams(params);

        }
        scrolling = false;
    }

    /**
     * 差值器补全view，大于一半展开，小于一半隐藏
     * @param view
     */
    private void InterceptView(View view){

        scrolling=true;
        smoothScrollTo(offset>10?viewheight:0);
//        AnimateView(view,offset>10?true:false);
        scrolling=false;
    }

    private void AnimateView(View childs,boolean isAdd){//
          /*  for(int i=offset;isAdd?i<20:i>0;i=(isAdd?(i+2):(i-2))){
                offset=i;
                params.topMargin= (int) (viewheight*((offset)/20f));
                childs.setLayoutParams(params);
                childs.requestLayout();
            }*/
        if(params!=null){
            params.topMargin=isAdd?viewheight:0;
            childs.setLayoutParams(params);
        }
       /* if(isAdd){//补全动画

        }else{//隐藏动画

        }*/
    }


    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisiableHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                setVisiableHeight((Integer) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public int getVisiableHeight() {
        int height = 0;
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mContainer
                .getLayoutParams();
        height = lp.height;
        return height;
    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        params.height = height;
        mContainer.setLayoutParams(params);
    }
}
