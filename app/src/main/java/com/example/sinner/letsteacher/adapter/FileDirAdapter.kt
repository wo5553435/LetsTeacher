package com.example.sinner.letsteacher.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.entity.FileBean

/**
 * Created by sinner on 2017-09-05.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
class FileDirAdapter(var datas:List<FileBean>,var onEventClick:OnEventClick) :RecyclerView.Adapter<FileDirAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_text, parent, false), onEventClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_name?.text=datas[position].name
    }

    override fun getItemCount()=(datas?.size)

    inner class ViewHolder(itemView: View, private val onEventClick: FileDirAdapter.OnEventClick?) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var tv_name:TextView?=null
        init {
            tv_name= itemView.findViewById(R.id.tv_item_text) as TextView?
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            onEventClick?.onItemClick(view, adapterPosition)
        }

    }

    abstract class OnEventClick {
        abstract fun onItemClick(view: View, position: Int)
    }
}