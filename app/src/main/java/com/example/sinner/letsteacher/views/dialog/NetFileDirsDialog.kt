package com.example.sinner.letsteacher.views.dialog

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.adapter.FileDirAdapter
import com.example.sinner.letsteacher.entity.FileBean

/**
 * Created by sinner on 2017-09-05.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
class NetFileDirsDialog :Dialog {
    var rv :RecyclerView ?=null
    var data:List<FileBean>
    var listener :ActionClick?=null
    constructor(context: Context,  data:List<FileBean>,res:Int=0):super(context,res){
        this.data=data
        init()
    }


    fun  init(){
        setContentView(R.layout.layout_dialog_netfiledir)
        rv= findViewById(R.id.rv_files) as RecyclerView?
        rv?.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        rv?.adapter=FileDirAdapter(data, object :FileDirAdapter.OnEventClick(){
            override fun onItemClick(view: View, position: Int) {
                listener?.onItemClick(position)
            }
        })
    }

    abstract  class ActionClick {
       abstract fun onItemClick(position: Int)
    }
}