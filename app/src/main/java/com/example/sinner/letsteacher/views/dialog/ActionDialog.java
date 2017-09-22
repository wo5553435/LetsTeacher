package com.example.sinner.letsteacher.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.adapter.FileDirAdapter;

/**
 * Created by sinner on 2017-09-05.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class ActionDialog extends Dialog implements View.OnClickListener {
    private TextView tv_1,tv_2,tv_3,tv_4;
    private OnActionClick listener;
    public ActionDialog(@NonNull Context context) {
        super(context);init();
    }

    public ActionDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);init();
    }

    protected ActionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);init();
    }

    private void init(){
        setContentView(R.layout.layout_dialog_action);
        tv_1= (TextView) findViewById(R.id.tv_action1);
        tv_2= (TextView) findViewById(R.id.tv_action2);
        tv_3= (TextView) findViewById(R.id.tv_action3);
        tv_4= (TextView) findViewById(R.id.tv_action4);
        tv_4.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_1.setOnClickListener(this);
        RecyclerView view;
    }

    public void SetListener(OnActionClick actionClick){
        this.listener=actionClick;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null)
        switch (view.getId()){
            case R.id.tv_action1:
                listener.OnItemClick(1);
                dismiss();
                break;
            case R.id.tv_action2:
                listener.OnItemClick(2);
                dismiss();
                break;
            case R.id.tv_action3:
                listener.OnItemClick(3);
                dismiss();
                break;
            case R.id.tv_action4:
                listener.OnItemClick(4);
                dismiss();
                break;
        }
    }

    public interface OnActionClick{
        void OnItemClick(int position);
    }
}
