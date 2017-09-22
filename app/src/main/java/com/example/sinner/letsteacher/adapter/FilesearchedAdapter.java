package com.example.sinner.letsteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sinner.letsteacher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sinner on 2017-07-03.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class FilesearchedAdapter extends RecyclerView.Adapter<FilesearchedAdapter.ViewHolder> {

    private List<String> data;
    private OnEventClick onEventClick;
    private SparseArray<String> selectitems;
    private boolean isSelectMode=false;
    public FilesearchedAdapter(List<String> data, OnEventClick onEventClick){
        this.data=data;
        this.onEventClick=onEventClick;
        selectitems=new SparseArray<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_searchfile, parent, false), onEventClick);

    }



    public void SetSelectMode(boolean flag){
        this.isSelectMode=flag;
        notifyDataSetChanged();
        if(!flag)
        selectitems.clear();
    }

    public boolean isSelectMode(){
        return isSelectMode;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if(!payloads.isEmpty()&&data!=null){


        }
        onBindViewHolder(holder,position);
        //super.onBindViewHolder(holder, position, payloads);
    }

    public ArrayList<Integer> getSelect(){
        ArrayList<Integer> result=new ArrayList<>();
        for (int i = 0; i < selectitems.size(); i++) {
            int keyindex=selectitems.keyAt(i);
            if(!"0".equals(selectitems.get(keyindex)))
                result.add(keyindex);
        }
        return result;
    }

    @Override
    public void onBindViewHolder (final ViewHolder holder, final int position){
        if(data!=null){
            holder.tv_file_name.setText(data.get(position));
            Glide.with(holder.mContext).load(data.get(position)).override(100,100).into(holder.img_file);
             holder.img_select.setVisibility(isSelectMode?View.VISIBLE:View.GONE);
            holder.img_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(!"0".equals(selectitems.get(position,"0"))){
                        selectitems.put(position,"0");
                        holder.img_select.setChecked(false);
                    }else{
                        holder.img_select.setChecked(true);
                        selectitems.put(position,"1");
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (data != null) return data.size();
        return 0;
    }
//abstract int getItemLayout();


    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private OnEventClick onEventClick;
        private ImageView img_file;
        private CheckBox img_select;
        private TextView tv_file_name;
        private Context mContext;
        public ViewHolder(View itemView,OnEventClick onEventClick) {
            super(itemView);
           mContext= itemView.getContext();
            img_file= (ImageView) itemView.findViewById(R.id.img_file_avater);
            tv_file_name= (TextView) itemView.findViewById(R.id.tv_file_name);
            img_select= (CheckBox) itemView.findViewById(R.id.check_file_select);
            this.onEventClick=onEventClick;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View view) {
            //getPosition 位置出现偏差，和adapterposition会因为你addview导致下标不正确
            if(onEventClick!=null)
            onEventClick.onItemClick(view,getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if(onEventClick!=null) onEventClick.onItemLongClick(view,getAdapterPosition()-1);
            return true;
        }
    }

    public static abstract  class OnEventClick {
        public abstract void onItemClick(View view, int position);
        public abstract  void onItemLongClick(View view,int position);
    }
}
