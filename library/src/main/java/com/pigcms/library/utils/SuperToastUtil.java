package com.pigcms.library.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.github.johnpersano.supertoasts.SuperToast;
import com.pigcms.library.R;

/**
 * Created by win7 on 2016-10-17.
 */

public class SuperToastUtil {
    private SuperToast superToast;
    Typeface typefaceLatoLight = null;
    private static  SuperToastUtil instance;
    private Context context;


    public static SuperToastUtil  getInstance(Context context){
        if(instance==null)
            instance=new SuperToastUtil(context);
        return  instance;
    }

    private SuperToastUtil(Context context){
        this.context=context.getApplicationContext();
        initToast();
    }

    private void initToast() {

        superToast = new SuperToast(context);
        typefaceLatoLight = Typeface.createFromAsset(
                context.getAssets(), "fonts/Lato-Regular.ttf");
    }

    public void showToast(String msg) {
        SuperToast.cancelAllSuperToasts();

        superToast.setAnimations(SuperToast.Animations.POPUP);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);
        superToast.setText(msg);
        superToast.setBackground(R.drawable.circle_blue_bg_10dp);
        // superToast.setBackground(SuperToast.Background.BLUE);
        superToast.getTextView().setTypeface(typefaceLatoLight);
        superToast.show();

    }

    public void showToast(int type ,String msg) {
        SuperToast.cancelAllSuperToasts();

        if(type==1)
            superToast.setAnimations(SuperToast.Animations.FLYIN);
        else if(type==2)
            superToast.setAnimations(SuperToast.Animations.POPUP);
        else if(type==3)
            superToast.setAnimations(SuperToast.Animations.FADE);
        else if(type==4)
            superToast.setAnimations(SuperToast.Animations.SCALE);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);
        superToast.setText(msg);
        superToast.setBackground(R.drawable.circle_blue_bg_10dp);
        // superToast.setBackground(SuperToast.Background.BLUE);
        superToast.getTextView().setTypeface(typefaceLatoLight);
        superToast.show();

    }

    public void showToast(int msgid) {
        SuperToast.cancelAllSuperToasts();
        superToast.setAnimations(SuperToast.Animations.POPUP);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);
        superToast.setText(context.getString(msgid));
        superToast.setBackground(R.drawable.circle_blue_bg_10dp);
        // superToast.setBackground(SuperToast.Background.BLUE);
        superToast.getTextView().setTypeface(typefaceLatoLight);
        superToast.show();

    }
}
