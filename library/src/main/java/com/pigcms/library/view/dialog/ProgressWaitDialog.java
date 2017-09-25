package com.pigcms.library.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.pigcms.library.R;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by sinner on 2017-09-25.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class ProgressWaitDialog extends Dialog {
    public ProgressWaitDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public ProgressWaitDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init(){
        setContentView(R.layout.wait_progress_content);
//        MaterialProgressBar pr= (MaterialProgressBar) findViewById(R.id.prb);
    }

}
