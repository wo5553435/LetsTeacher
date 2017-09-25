package com.example.sinner.letsteacher.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.entity.FileBean;
import com.example.sinner.letsteacher.utils.StringTools;

import java.util.List;
/**
 * Created by sinner on 2017-06-27.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {


    private List<FileBean> data;
    private OnEventClick onEventClick;


    public FileListAdapter(List<FileBean> data, OnEventClick onEventClick) {
        this.data = data;
        this.onEventClick = onEventClick;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_file, parent, false), onEventClick);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_add.setVisibility(position == data.size() ? View.VISIBLE : View.GONE);
        holder.tv_name.setVisibility(position == data.size() ? View.GONE : View.VISIBLE);
        if (data != null && position != data.size()) {
            holder.tv_name.setText(data.get(position).getName());
            holder.tv_name.setTextSize(14);
            if (StringTools.INSTANCE.getNotNullStr(data.get(position).getColorids()).length() != 0) {
                GradientDrawable drawable = (GradientDrawable) holder.tv_name.getBackground();
                drawable.setColor(Color.parseColor(data.get(position).getColorids()));
                drawable.setStroke(1, Color.WHITE);
            }
        } else {
            holder.tv_name.setTextSize(30);
        }
    }


    @Override
    public int getItemCount() {//至多只显示8个
        if (data != null)
            if (data.size() >= 8) return 8;
            else
                return data.size() + 1;
        return 0;
    }
//abstract int getItemLayout();


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnEventClick onEventClick;
        TextView tv_name, tv_add;

        public ViewHolder(View itemView, OnEventClick onEventClick) {
            super(itemView);
            this.onEventClick = onEventClick;
            tv_name = (TextView) itemView.findViewById(R.id.tv_flieitem_name);
            tv_add = (TextView) itemView.findViewById(R.id.tv_flieitem_add);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            //getPosition 位置出现偏差，和adapterposition会因为你addview导致下标不正确
            if (onEventClick != null)
                onEventClick.onItemClick(view, getAdapterPosition());
        }
    }

    public static abstract class OnEventClick {
        public abstract void onItemClick(View view, int position);
    }

}
