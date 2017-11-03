package com.example.sinner.letsteacher.utils

import android.support.v7.util.DiffUtil
import com.example.sinner.letsteacher.activity.clearboom.BoomItem

/**
 * Created by sinner on 2017-10-28.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
class DiffCallback(val olddatas:List<BoomItem>,val newdatas:List<BoomItem>) :DiffUtil.Callback(){


    override fun getOldListSize()=(olddatas?.size)

    override fun getNewListSize()=(newdatas?.size)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        var olddata=olddatas[oldItemPosition] ;var newdata=newdatas[newItemPosition];
        return (olddata.isclick==newdata.isclick)&&(olddata.isshow==newdata.isshow)
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)=
            (olddatas.get(oldItemPosition).x==newdatas.get(newItemPosition).x)&&(olddatas.get(oldItemPosition).y==newdatas.get(newItemPosition).y)

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        if (olddatas[oldItemPosition].isclick==newdatas[newItemPosition].isclick&&olddatas[oldItemPosition].isshow==newdatas[newItemPosition].isshow)
            return null
        return 1
    }
}