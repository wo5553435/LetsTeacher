package com.example.sinner.letsteacher.activity.clearboom

/**
 * Created by sinner on 2017-10-13.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
data class BoomItem(val x:Int, var y:Int, var isBoom:Boolean,var index:Int){
    var roundcount=0//八方向周围雷数
    var isshow=false//是否已经翻开
    var isclick=false;//是否被点击(雷专属)
    var local=Pair(x,y)
}