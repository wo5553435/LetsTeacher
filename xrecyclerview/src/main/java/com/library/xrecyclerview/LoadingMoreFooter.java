package com.library.xrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.library.xrecyclerview.progressindicator.AVLoadingIndicatorView;
import com.zhou.sinner.xrecyclerview.R;


public class LoadingMoreFooter extends LinearLayout {

    private SimpleViewSwithcer progressCon;
    private Context mContext;
    public final static int STATE_LAODING = 0;
    public final static int STATE_LOADING_OVER = 3;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    private TextView mText;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView(context);
    }

    public LoadingMoreFooter(Context context,int Colorid){
        super(context);
        initView(context);
        SetBackgroundColor(Colorid);
    }

    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        mContext = context;
        setGravity(Gravity.CENTER);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.title_height)));
        progressCon = new SimpleViewSwithcer(context);
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(context.getResources().getColor(R.color.white));
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        progressCon.setView(progressView);

        addView(progressCon);
        mText = new TextView(context);
        mText.setText(context.getResources().getString(R.string.listview_loading));
        mText.setTextColor(context.getResources().getColor(R.color.menu_blue));

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) getResources().getDimension(R.dimen.shake_left_and_right), 0, 0, 0);

        mText.setLayoutParams(layoutParams);
        addView(mText);

        this.setBackgroundColor(context.getResources().getColor(R.color.white));
    }


    public void SetBackgroundColor(int colorid){

        this.setBackgroundColor(mContext.getResources().getColor(colorid));
    }

    public void setProgressStyle(int style) {
        if (style == ProgressStyle.SysProgress) {
            progressCon.setView(new ProgressBar(mContext, null, android.R.attr.progressBarStyle));
        } else {
            AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffffffff);
            progressView.setIndicatorId(style);
            progressCon.setView(progressView);
        }
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LAODING:
                progressCon.setVisibility(View.VISIBLE);
                mText.setText(mContext.getText(R.string.listview_loading));
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(mContext.getText(R.string.listview_loading));
                this.setVisibility(View.GONE);
                break;
            case STATE_LOADING_OVER:
                mText.setText(mContext.getString(R.string.listview_loading_over));
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_NOMORE:
                mText.setText(mContext.getText(R.string.nomore_loading));
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;

        }

    }
}
