package com.pigcms.library.bottomfragment;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.pigcms.library.utils.Logs;

/**
 * Created by sinner on 2017-06-28.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public abstract class CollapseFragment extends BottomSheetDialogFragment {
    View contentView;
    CoordinatorLayout.Behavior behavior;

    abstract  int getLayoutId();
    abstract  void initView();
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        Logs.e("setupdialog", "----");
        contentView = View.inflate(getContext(), getLayoutId(), null);
        initView();
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        behavior= params.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);

        }
    }

    public void setPeekHeight(int peekHeight){
        ((BottomSheetBehavior) behavior).setPeekHeight(peekHeight);
    }


}
