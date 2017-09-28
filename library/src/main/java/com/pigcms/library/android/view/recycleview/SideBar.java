package com.pigcms.library.android.view.recycleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by sinner on 2017-07-06.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class SideBar extends View {
    //List<Float> pos;
    float [] pos={20f,25f,20f,50f,20f,75f,20f,100f,20f,125f,20f,150f,20f,175f,20f,200f,20f,225f,20f,250f
            ,20f,275f,20f,300f,20f,325f,20f,350f,20f,375f,20f,400f,20f,425f,20f,450f,20f,475f,20f,500f
            ,20f,525f,20f,550f,20f,575f,20f,600f,20f,625f,20f,650f,20f,675f};
    String[] chars={ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z","#"};
    private int width;
    private int height;
    private float cellHeight;//每个单词的高度


    private int textsize=22;
    private Rect textRect;
    private float touchX;
    private float touchY;

    private boolean isTouch =false;//是否是触摸状态

    private String lastTouchStr="",current="0";
    SelectListener listener;

    String indexStr;
    Paint textpaint;

    public SideBar(Context context) {
        super(context);
        init();
    }


    public SideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setSelectListener(SelectListener listener){
        this.listener=listener;
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(measureWidth, measureHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //不知道为什么这个方法要弃用了 不然和pos结合判断很好定位
//        canvas.drawPosText(indexStr,pos,textpaint);
        DrawTextOnLine(canvas);
    }

    private void DrawTextOnLine(Canvas canvas){
        for (int i = 0; i < chars.length; i++) {
            String char_=chars[i];
            textpaint.getTextBounds(char_,0,1,textRect);
            canvas.drawText(char_,(width-textRect.width())/2f,cellHeight*i+(cellHeight+textRect.height())/2,textpaint);
        }
    }


    private void init() {
        textRect=new Rect();
       // indexStr=getContext().getResources().getString(R.string.key_case);
//        chars=getContext().getResources().getStringArray(R.array.permissions);'
//        data= Arrays.asList();
//        pos=new ArrayList<>();
//        for(int i=0;i<27;i++){
//            pos.add(20f);
//            pos.add(25f*i);
//        }
//        textpaint.setTextAlign(Paint.Align.CENTER);
        textpaint=new Paint();
        textpaint.setTextSize(textsize);
        textpaint.setAntiAlias(true);
        textpaint.setColor(Color.WHITE);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(changed){
            width=getMeasuredWidth();
            height=getMeasuredHeight();
            cellHeight=height*1.0f/chars.length;
            textsize=(int)((width>cellHeight?cellHeight:width)*(3f/4));//手动计算文字高度
            textpaint.setTextSize(textsize);
        }

        //super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isTouch=true;
                touchX=event.getX();
                touchY=event.getY();
                current=getSelect();
                if(listener != null &&!current.equals(lastTouchStr)){
                    listener.OnSelect(getSelect());
                    lastTouchStr=current;
                }
//                if(listener != null && touchX < 0){
//                    listener.OnSelect(getSelect());
//                }
                return true;
            case MotionEvent.ACTION_UP:
                isTouch=false;
                lastTouchStr="0";
                touchY = event.getY();
                if(listener != null){
                    listener.OnSelect(getSelect());
                }

                return true;
        }
        return super.onTouchEvent(event);
    }

    public boolean isTouchStatu(){
        return isTouch;
    }

    private String getSelect(){

        int index= (int) (touchY/cellHeight);
        if(index<0) index=0;
        if(index>=chars.length) index=chars.length-1;
        return  chars[index];
    }


    public interface  SelectListener{
        void OnSelect(String str);
    }
}
