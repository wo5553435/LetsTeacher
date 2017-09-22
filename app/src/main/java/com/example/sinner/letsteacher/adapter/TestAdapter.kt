package com.example.sinner.letsteacher.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.example.sinner.letsteacher.R

import java.util.ArrayList

import butterknife.BindBitmap
import butterknife.BindView
import butterknife.ButterKnife

/**
 * Created by win7 on 2017-02-27.
 */

class TestAdapter(test: Boolean, private val context: Context, private val onEventClick: OnEventClick) : RecyclerView.Adapter<TestAdapter.ViewHolder>() {
    private val data: ArrayList<Any>?
    private val drawables = intArrayOf(R.drawable.android, R.drawable.batman, R.drawable.daredevil, R.drawable.deadpool, R.drawable.gambit, R.drawable.hulk, R.drawable.mario, R.drawable.wolverine)


    init {
        data = ArrayList()
        for (i in 0..19) {
            data.add(Any())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.text_itemlayout, parent, false), onEventClick)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data != null) {
            //holder.img.setImageBitmap(drawables.get(position%8));
            //holder.img.setImageBitmap(holder.drawables.get(position%8));
            Glide.with(context).load(drawables[position % 8]).crossFade()
                    .into(holder.img)
            holder.textView!!.text = "测试" + position
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }
    //abstract int getItemLayout();


    inner class ViewHolder//        @BindBitmap(R.drawable.android)
    //        Bitmap drawable_android;
    //        @BindBitmap(R.drawable.batman) Bitmap drawable_batman;
    //        @BindBitmap(R.drawable.daredevil) Bitmap drawable_daredevil;
    //        @BindBitmap(R.drawable.deadpool) Bitmap drawable_deadpool;
    //        @BindBitmap(R.drawable.gambit) Bitmap drawable_gambit;
    //        @BindBitmap(R.drawable.hulk)  Bitmap drawable_hulk;
    //        @BindBitmap(R.drawable.mario) Bitmap drawable_mario;
    //        @BindBitmap(R.drawable.wolverine) Bitmap drawable_wolverine;
    //        List<Bitmap> drawables=new ArrayList<>();
    (itemView: View, private val onEventClick: OnEventClick?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        @BindView(R.id.test_item_img)
        internal var img: ImageView? = null
        @BindView(R.id.test_item_title)
        internal var textView: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
            //drawables.add(drawable_android);drawables.add(drawable_batman);drawables.add(drawable_daredevil);drawables.add(drawable_deadpool);
            //drawables.add(drawable_gambit);drawables.add(drawable_hulk);drawables.add(drawable_mario);drawables.add(drawable_wolverine);
            img!!.setOnClickListener(this)
        }


        override fun onClick(view: View) {
            onEventClick?.onItemClick(view, adapterPosition)
        }
    }

    abstract class OnEventClick {
        abstract fun onItemClick(view: View, position: Int)
    }
}
