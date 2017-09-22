package com.example.sinner.letsteacher.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sinner.letsteacher.R;

/**
 * Created by sinner on 2017-09-11.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class TextDialog extends Dialog implements View.OnClickListener {
    private EditText edtext;
    private Button btn_commit;
    OnResultListener listener;

    public TextDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public TextDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    public void setText(String str){
        edtext.setText(str);
    }

    public void setListener(OnResultListener listener){
        this.listener=listener;
    }

    private void init() {
        setContentView(R.layout.layout_dialog_text);
        edtext = (EditText) findViewById(R.id.ed_member_value);
        btn_commit = (Button) findViewById(R.id.bt_ok);
        btn_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.Done(edtext.getText().toString().trim());
            dismiss();
        }
    }

    public  interface OnResultListener {
        void Done(String str);
    }
}
