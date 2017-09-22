package com.example.sinner.letsteacher.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.example.sinner.letsteacher.R;
import com.pigcms.library.utils.Logs;

import static android.R.attr.data;

/**
 * Created by sinner on 2017-06-28.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class ColorSelectFragment extends BaseBottomFragment implements SeekBar.OnSeekBarChangeListener {
    SeekBar seekBar_red;
    SeekBar seekBar_green;
    SeekBar seekBar_blue;
    SeekBar seekBar_con;

    public ColorSelectFragment(){

    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_dialog_colorselect;
    }

    @Override
    public void initView() {
        seekBar_blue= (SeekBar) rootView.findViewById(R.id.seekBar3);
        seekBar_green= (SeekBar) rootView.findViewById(R.id.seekBar2);
        seekBar_red= (SeekBar) rootView.findViewById(R.id.seekBar);
        seekBar_con= (SeekBar) rootView.findViewById(R.id.seekBar_con);
        initListener();
    }

    private void initListener() {
        seekBar_con.setOnSeekBarChangeListener(this);
        seekBar_red.setOnSeekBarChangeListener(this);
        seekBar_green.setOnSeekBarChangeListener(this);
        seekBar_blue.setOnSeekBarChangeListener(this);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Logs.e("progress:"+i,"b"+b);
        switch (seekBar.getId()){
            case R.id.seekBar:
                ChangeViewStatu(seekBar,i,1);
                break;
            case R.id.seekBar2:
                ChangeViewStatu(seekBar,i,2);
                break;
            case R.id.seekBar3:
                ChangeViewStatu(seekBar,i,3);
                break;
            case R.id.seekBar_con:
                ChangeViewStatu(seekBar,i,0);
                break;
        }
    }


    private void ChangeViewStatu(SeekBar seekbar,int progress,int type){
        GradientDrawable drawable= (GradientDrawable) seekbar.getThumb().mutate();
        int percent=(int)(255*(progress/100f));
        switch (type){
            case 1:
                drawable.setColor(Color.rgb(255,percent,percent));
                break;
            case 2:
                drawable.setColor(Color.rgb(percent,255,percent));
                break;
            case 3:
                drawable.setColor(Color.rgb(percent,percent,255));
                break;
        }



//        drawable.setColor(Color.parseColor("#"+x+x+x+x+x+x));
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e("onStartTrackingTouch", "onStartTrackingTouch: " );
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e("onStopTrackingTouch", "onStartTrackingTouch: " );
    }
}
