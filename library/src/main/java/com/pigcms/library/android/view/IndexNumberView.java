package com.pigcms.library.android.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pigcms.library.R;
import com.pigcms.library.android.interfaces.NoSpecialCodeListener;
import com.pigcms.library.utils.AndroidUtil;
import com.pigcms.library.utils.ScreenUtil;
import com.pigcms.library.utils.StringTools;

/**
 * 最近发现可选数目控件开始需求变多 所以就准备封装下以后用免得以后改来改去
 * <p>
 * 但我没有在onmearsuer中做过多改动 宽度一定要给不然 就是全屏
 *
 * 特别提出需要动画优化 渐变过程 后期我会抽时间出来优化的
 * Created by win7 on 2017-04-27.
 */

public class IndexNumberView extends LinearLayout   {

    private int normalColor = 0;
    private int enableColor = 0;
    private Context context;
    private StateListDrawable addDrawable, reduceDrawable;//这是背景图（加上enable statu的图片资源）
    private int CurrentCount = 0;
    private int drawableheight = 0;
    private int textpadding = 0;
    private int textsize = 0;
    private int drawablesize = 0;
    private int maxvalue = 0;
    private int minvalue = 0;
    TextView textView;
    EditText editText;
    onVauleChange listener;
    private TextView showtext;
    EqualrationImageView reduce, add;
    private Drawable left, right;//背景图
    long  currenttime=0;//当前长按前的时间
    private boolean dosth=false;
    private boolean iseditMode=false;
    private boolean isAnimation=false;//是否在进行动画
    private int Width=0; //这里是能显示的宽度  主要用来调控动画后的宽度
    private boolean useIconBackground=true;
    public IndexNumberView(Context context) {
        super(context);
        init(context, null);

    }

    public IndexNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IndexNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void SetTextColor(int Colorids) {
       if(iseditMode){ editText.setTextColor(Colorids);}else textView.setTextColor(Colorids);
    }

    public int getNum() {
        return Integer.parseInt(iseditMode? StringTools.getNotNullStr(editText.getText().toString(),"0"): StringTools.getNotNullStr(textView.getText().toString(),"0"));
    }

    public void SetCurrent(int value) {
        SetValue(value,maxvalue,minvalue);
//        JudgeStatu();
//        postInvalidate();
    }

    public void SetValue(int currentvalue, int maxvalue, int minvalue) {
        this.CurrentCount = currentvalue;
        if(!iseditMode)
        textView.setText("" + currentvalue);
        else  editText.setText("" + currentvalue);
        this.maxvalue = maxvalue;
        this.minvalue = minvalue;
        JudgeStatu();
        postInvalidate();
    }

    public void SetMaxnum(int maxvalue) {
        this.maxvalue = maxvalue;
        invalidate();
    }

    public void SetMinnum(int minvalue) {
        this.minvalue = minvalue;
        invalidate();
    }
    public void SetClickEvent(final OnItemClickAction clickListener, final boolean extend){
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.AddAction();
                //if(extend) AddNum();
            }
        });

        reduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.ReduceAction();
               // if(extend) ReduceNum();
            }
        });
//        add.setOnClickListener(clickListener);
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndexNumberView);
        CurrentCount = a.getInteger(R.styleable.IndexNumberView_text, 0);
        addDrawable = (StateListDrawable) a.getDrawable(R.styleable.IndexNumberView_adddrawable);
        reduceDrawable = (StateListDrawable) a.getDrawable(R.styleable.IndexNumberView_reducedrawable);
        drawableheight = ScreenUtil.dip2px(getContext(), a.getInteger(R.styleable.IndexNumberView_drawableheight, 0));
        left = a.getDrawable(R.styleable.IndexNumberView_leftdrawable);
        right = a.getDrawable(R.styleable.IndexNumberView_rightdrawable);
        useIconBackground=a.getBoolean(R.styleable.IndexNumberView_useicon,true);
        // Width=((ViewGroup)getParent()).getLayoutParams().width;
        Width= AndroidUtil.dip2px(getContext(),a.getInteger(R.styleable.IndexNumberView_maxWidth,getMeasuredWidth()));
        iseditMode=a.getBoolean(R.styleable.IndexNumberView_edit,false);
        if (attrs == null) {
            normalColor = Color.parseColor("#E4B256");
            enableColor = Color.parseColor("#E7E7E7");
        }
        if (reduceDrawable == null) {
            if (addDrawable == null)
                addDrawable =/* new StateListDrawable();*/ (StateListDrawable) context.getResources().getDrawable(R.drawable.selector_bg_btnstatu);
            reduceDrawable =/*new StateListDrawable();*/ (StateListDrawable) context.getResources().getDrawable(R.drawable.selector_bg_btnstatu);
        }
        if (left == null)
            left = context.getResources().getDrawable(R.drawable.ic_action_left);
        if (right == null)
            right = context.getResources().getDrawable(R.drawable.ic_action_right);

        CurrentCount = a.getInteger(R.styleable.IndexNumberView_text, 0);
        textsize = a.getInteger(R.styleable.IndexNumberView_text_Size, 18);
        maxvalue = a.getInteger(R.styleable.IndexNumberView_max, Integer.MAX_VALUE);
        minvalue = a.getInteger(R.styleable.IndexNumberView_minum, 0);

        LayoutParams layoutParams = new LayoutParams(drawableheight == 0 ? 0 : drawableheight, drawableheight == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : drawableheight);
        layoutParams.gravity = Gravity.CENTER;
        if (drawableheight == 0) layoutParams.weight = 1;

        LayoutParams textlayouManager = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);//中间文字显示params
        textlayouManager.gravity = Gravity.CENTER;
        textlayouManager.leftMargin = 12;
        textlayouManager.rightMargin = 12;
        textlayouManager.weight = 1;
      /*  LayoutParams shiwlayouManager = new LayoutParams(*//*ViewGroup.LayoutParams.MATCH_PARENT*//*0, ViewGroup.LayoutParams.MATCH_PARENT);
        shiwlayouManager.gravity = Gravity.CENTER;
        shiwlayouManager.leftMargin = 20;
        shiwlayouManager.rightMargin = 20;
        shiwlayouManager.topMargin=10;
        shiwlayouManager.bottomMargin=10;*/

        //right button
        reduce = new EqualrationImageView(context);
        reduce.setBackgroundDrawable(reduceDrawable);
        if(useIconBackground)
        reduce.setImageDrawable(left);
        reduce.setLayoutParams(layoutParams);
//        reduce.setTextColor(Color.WHITE);
//        reduce.setTextSize(textsize);
//        reduce.setGravity(Gravity.CENTER);
        reduce.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addView(reduce);


        //text
        if(!iseditMode) {
            textView = new TextView(context);
            textView.setLayoutParams(textlayouManager);
            textView.setText(String.valueOf(CurrentCount));
            textView.setTextSize(textsize);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            addView(textView);
        }else{
            editText=new EditText(context);
            editText.setLayoutParams(textlayouManager);
            editText.setText(String.valueOf(CurrentCount));
            editText.setTextSize(textsize);
            editText.setGravity(Gravity.CENTER);
            editText.setEnabled(true);
            editText.setMaxLines(1);
            editText.setTextColor(Color.BLACK);
            editText.setBackgroundResource(0);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setFilters(NoSpecialCodeListener.getInstance().Filter());
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int num=1;
                    try {
                         num=Integer.parseInt(editText.getText().toString());
                        if(num>maxvalue) {editText.setText(""+maxvalue); num=maxvalue;}
                        if(num<minvalue) {editText.setText(""+minvalue);num=minvalue;}

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    editText.setSelection(editText.getText().length());
                    CurrentCount=num;
                    JudgeStatu();
                }
            });
            addView(editText);
        }




        //left button
        add = new EqualrationImageView(context);
        add.setBackgroundDrawable(addDrawable);
        add.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if(useIconBackground)
        add.setImageDrawable(right);
//        add.setTextColor(Color.WHITE);
//        add.setGravity(Gravity.CENTER);
//        add.setTextSize(textsize);
        //注意单位是px
        add.setLayoutParams(layoutParams);
        addView(add);


//        showtext = new TextView(context);
//        showtext.setLayoutParams(shiwlayouManager);
//        showtext.setText(String.valueOf("加入购物车"));
//        showtext.setTextSize(textsize);
//        showtext.setGravity(Gravity.CENTER);
//        showtext.setTextColor(Color.BLACK);
//        addView(showtext);


        a.recycle();
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAnimation)
                AddNum();
            }
        });
        reduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAnimation)
                ReduceNum();
            }
        });


        add.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        currenttime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        long time=System.currentTimeMillis();
                        if(currenttime!=0&&time-currenttime>3000){
                            SetCurrent((maxvalue>50)?50:maxvalue);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        currenttime = 0;
                        break;
                }
                return false;
            }
        });

        reduce.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        currenttime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        long time=System.currentTimeMillis();
                        if(currenttime!=0&&time-currenttime>3000){
                            SetCurrent(minvalue);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        currenttime = 0;
                        break;
                }
                return false;
            }
        });

//        add.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                SetCurrent((maxvalue>9999)?9999:maxvalue);
//                return false;
//            }
//        });
//
//        reduce.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                SetCurrent(minvalue);
//                return false;
//            }
//        });

        JudgeStatu();
        initanimation();
    }

    private void initanimation() {
        animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.setInterpolator(new FastOutSlowInInterpolator());

        afterscaleX = ObjectAnimator.ofFloat(iseditMode?editText:textView, "scaleX", 0.5f, 1.0f);
        afterscaleY = ObjectAnimator.ofFloat(iseditMode?editText:textView, "scaleY", 0.5f, 1.0f);
        afterscaleX.setDuration(200);
        afterscaleY.setDuration(200);
        afterscaleX.setInterpolator(new FastOutSlowInInterpolator());
        afterscaleY.setInterpolator(new FastOutSlowInInterpolator());
        animatorSet.play(afterscaleX).with(afterscaleY);

    }

    public void SetAnimationEnable(boolean flag) {
        showAnimation = flag;
    }

    public void AddNum() {
        if(!iseditMode)
        textView.setText("" + (++CurrentCount));
        else editText.setText("" + (++CurrentCount));
        JudgeStatu();
        if (listener != null) listener.onvaluechange(true,CurrentCount);
        postInvalidate();
        if (showAnimation) {
            StartAnimation();
        }
    }

    private void StartAnimation() {
        if (animatorSet.isRunning())
            animatorSet.end();
        animatorSet.start();
    }

    public void ReduceNum() {

        if(!iseditMode)
        textView.setText("" + (--CurrentCount));
        else editText.setText("" + (--CurrentCount));
        JudgeStatu();
        if (listener != null) listener.onvaluechange(false,CurrentCount);
        postInvalidate();
        if (showAnimation)
            StartAnimation();

    }

//    private void Changet

    AnimatorSet animatorSet = new AnimatorSet();//组合动画
    ObjectAnimator afterscaleX;
    ObjectAnimator afterscaleY;

    ScaleAnimation animation;
    boolean isAdd = false;
    boolean showAnimation = true;


    public void ChangeText() {
        if (!isAdd) textView.setText("" + (--CurrentCount));
        else textView.setText("" + (++CurrentCount));
        JudgeStatu();
        if (listener != null) listener.onvaluechange(isAdd,CurrentCount);
        postInvalidate();
    }


    private void JudgeStatu() {
        if (CurrentCount >= maxvalue)
            add.setEnabled(false);
        else
            add.setEnabled(true);
        if (CurrentCount <= minvalue)
            reduce.setEnabled(false);
        else
            reduce.setEnabled(true);


    }

    private boolean isOverNum(){
        return (CurrentCount < maxvalue||CurrentCount > minvalue);
    }

    public interface onVauleChange {
        void onvaluechange(boolean add, int position);
    }


    public void setValueChangeListener(onVauleChange listener) {
        this.listener = listener;
    }

    public int getCurrentCount() {
        return CurrentCount;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int measureHeigt = MeasureSpec.getSize(heightMeasureSpec);
//        Logs.e("width:"+measureWidth,"height:"+measureHeigt);
//        add.setText(" + ");
//        reduce.setText(" - ");
//    }

    /**
     * 持续性的增加或者减少数字
     * @param isAdd
     */
    private void ChangeNumStatu(boolean isAdd){
        if(isAdd){

        }else{

        }
    }

    ValueAnimator va;

    /**
     * 从左或从右缩放  这个动画没写好 很是丑陋 建议不要用
     * @param isshow
     * @param isleft
     * @param action
     */
    public void ChangeStatu(final boolean isshow, final boolean isleft, final AnimationEndAction action){
//        final int width1=add.getMeasuredWidth();
//        final int width2=iseditMode?editText.getMeasuredWidth():textView.getMeasuredWidth();
//        final int width3=reduce.getMeasuredWidth();
        va=isshow?ValueAnimator.ofFloat(0.0f,1f):ValueAnimator.ofFloat(1f,0.0f);
        va.setInterpolator(new FastOutSlowInInterpolator());
        va.setDuration(800);
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimation=true;
                AnimatieReduceView (isshow);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimation=false;
                if(action!=null)action.Action();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isAnimation=false;
                if(action!=null)action.Action();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                LayoutParams params= ((LayoutParams)textView.getLayoutParams());
                params.weight=((float)(valueAnimator.getAnimatedValue()));
                textView.setLayoutParams(params);

                if(isleft){
//                    textView.setLayoutParams(textView.getLayoutParams().,).width= (int) (width2*(float)(valueAnimator.getAnimatedValue()));
//                    add.getLayoutParams().width= (int) (width1*(float)(valueAnimator.getAnimatedValue()));
                }else{

                }
            }
        });
        va.start();
    }

    /**
     * 在编辑模式和显示模式中展示 整体的缩放
     */
    public void ChangeStatu(boolean isshow, final AnimationEndAction action){

        final ViewGroup.LayoutParams params=  getLayoutParams();
        if(isshow){//
            va=ValueAnimator.ofFloat(0.0f,1f);
            va.setInterpolator(new FastOutSlowInInterpolator());
            va.setDuration(800);
            va.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    isAnimation=true;
                    AnimatieReduceView (true);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isAnimation=false;
                    if(action!=null)action.Action();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    isAnimation=false;
                    if(action!=null)action.Action();
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    params.width= (int) (Width*((float) valueAnimator.getAnimatedValue()));
                   setLayoutParams(params);

                }
            });

        }else{
            va=ValueAnimator.ofFloat(1f,0.0f);
            va.setInterpolator(new FastOutSlowInInterpolator());
            va.setDuration(800);
            va.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    AnimatieReduceView (false);
                    isAnimation=true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isAnimation=false;
                    if(action!=null)action.Action();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    isAnimation=false;

                    if(action!=null)action.Action();
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    params.width= (int) (Width*((float) valueAnimator.getAnimatedValue()));
                    setLayoutParams(params);
                }
            });
        }
        va.start();
    }

    public interface  AnimationEndAction{
        void Action();
    }




    private void AnimatieAddView(boolean direction){
        if(direction){
            ViewCompat.animate(add).rotation(360).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        }else{
            ViewCompat.animate(add).rotation(-360).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        }
    }

    private void AnimatieReduceView(boolean direction){
        if(direction){
            ViewCompat.animate(reduce).rotation(360).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        }else{
            ViewCompat.animate(reduce).rotation(-360).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        }
    }

    public interface  OnItemClickAction{
        void AddAction();
        void ReduceAction();
    }

}
