package com.example.sinner.letsteacher.activity.clearboom

import android.content.Context
import android.util.ArrayMap
import java.util.*

/**
 * Created by sinner on 2017-09-17.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
class BoomManager(var context: Context) {
    val allmap=ArrayMap<Pair<Int,Int>,BoomView>()//boom全表
    val boommap=ArrayMap<Pair<Int,Int>,BoomView>()//boom表
    var currentlevel=Leves.EASY

    fun getrandomBoom(){
        allmap.clear()
       val size=Math.sqrt(currentlevel.max.toDouble()).toInt()-1
       for(x in 0..size){
           for(y in 0..size){
               allmap.put(Pair(x,y), BoomView(context))
           }
       }
        val maxboomsize=0
        while (boommap.size<=currentlevel.count){
            var bx:Int= (10*Math.random()*(size+1)).toInt()
            var by=(10*Math.random()*(size+1)).toInt()
            var key=Pair(bx,by)
            if(!boommap.contains(key)){
                allmap.get(key)?.isBoom=true
                boommap.put(key,allmap.get(key))
            }
        }
    }



    /**
     * 单纯地计算有几个雷
     */
    fun getRoundBoom( x :Int, y:Int):Int{
        var count=0;
        var length=Math.sqrt(currentlevel.max.toDouble()).toInt()
        count=with(Pair<Int,Int>(x,y)){
            if(x-1>0&&y-1>0) if (boommap.get(Pair(x-1,y-1))?.isBoom!!) count++ //左上
            if(y-1>0)        if(boommap.get(Pair(x,y-1))?.isBoom!!) count++//中上
            if(y-1>0&&x+1<length)        if(boommap.get(Pair(x,y-1))?.isBoom!!) count++//右上
            if(x-1>0)        if(boommap.get(Pair(x,y-1))?.isBoom!!) count++//中左
            if(x+1>0)        if(boommap.get(Pair(x,y-1))?.isBoom!!) count++//中右
            if(x-1>0&&y+1<length)        if(boommap.get(Pair(x,y-1))?.isBoom!!) count++//下左
            if(y+1<length)        if(boommap.get(Pair(x,y-1))?.isBoom!!) count++//下中
            if(x+1<length&&y+1<length)        if(boommap.get(Pair(x,y-1))?.isBoom!!) count++//下右
            count
        }
        return count
    }

    enum class Leves(val max:Int,val count:Int){
        EASY(10,64),NORMAL(20,100),HARD(40,144)
    }
}