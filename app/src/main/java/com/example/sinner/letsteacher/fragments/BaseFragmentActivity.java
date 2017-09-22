package com.example.sinner.letsteacher.fragments;

import android.support.v4.app.FragmentActivity;

/**
 * Created by win7 on 2017-06-14.
 */

public class BaseFragmentActivity extends FragmentActivity {
    @Override
    public void onBackPressed() {
        finish();
    }
}
