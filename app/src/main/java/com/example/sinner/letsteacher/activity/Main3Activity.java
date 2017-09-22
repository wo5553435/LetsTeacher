package com.example.sinner.letsteacher.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.adapter.TestAdapter;
import com.example.sinner.letsteacher.interfaces.behavior.BottomSheetView;

/**
 * Created by win7 on 2017-03-13.
 */

public class Main3Activity extends AppCompatActivity {


    private Button btn_window;
    View mBottomSheet;
    BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private BottomSheetView sheetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        btn_window = (Button) findViewById(R.id.btn_window);
        mBottomSheet = findViewById(R.id.bottomSheet);
        RecyclerView recyclerView = (RecyclerView) mBottomSheet.findViewById(R.id.recyclerview);
        mBehavior = BottomSheetBehavior.from(mBottomSheet);

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        /*recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ItemAdapter(createItems()));*/
        btn_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(sheetView.isShowing())
                        sheetView.dismiss();
                else  sheetView.show();
            }
        });

        sheetView=new BottomSheetView(this);
        sheetView.setPeekHeight(400);
        View view= LayoutInflater.from(this).inflate(R.layout.layout_test_cardview,null);
        RecyclerView rv= (RecyclerView) view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new TestAdapter(true,this,null));
        sheetView.setContentView(view);

    }

}
