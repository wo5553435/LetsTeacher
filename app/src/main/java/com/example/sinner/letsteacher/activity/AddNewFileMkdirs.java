package com.example.sinner.letsteacher.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sinner.letsteacher.R;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sinner on 2017-06-29.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class AddNewFileMkdirs extends BasicActivity {
    private HashMap<String,File> files;

    @BindView(R.id.layout_search_local)
    LinearLayout layout_gosearch;
    @BindView(R.id.rv_fileadd_data)
    RecyclerView recyclerView;

    @Override
    protected int getContentLayout() {
        return R.layout.layout_activity_addnewfile;
    }

    @Override
    protected void initGui() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));

    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initAction() {

    }

    private void GotoSearchFile(){
        Intent intent=new Intent(activity,SearchFileActivity.class);
        startActivityForResult(intent ,2);
    }


    @OnClick({R.id.layout_search_local})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.layout_search_local:
                startActivity(new Intent(activity,SearchFileActivity.class));
                break;
        }
    }
}
