package com.example.sinner.letsteacher.activity.clearboom

import android.content.Context
import android.util.ArrayMap
import android.util.SparseArray
import com.example.sinner.letsteacher.utils.Logs
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by sinner on 2017-09-17.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 *
 */
class BoomManager(var context: Context) {
    val allmap = ArrayMap<Pair<Int, Int>, BoomItem>()//boom全表
    val boommap = ArrayMap<Pair<Int, Int>, BoomItem>()//boom表
    var currentlevel = Levels.NORMAL
    var checkarray = LinkedBlockingQueue<BoomItem>()//需要检验的数组
    var checkedarray = LinkedBlockingQueue<BoomItem>()// 完成校验的数组
    var isGameOver = false;
    var knowdata = ArrayList<BoomItem>()//已经完成身份验证的数据组
    var boomdata: ArrayList<BoomItem>? = null

    fun getrandomBoom() {
        allmap.clear()
        val xsize = currentlevel.xcount - 1
        val ysize = currentlevel.ycount - 1
        var index = 0
        for (x in 0..xsize) {
            for (y in 0..ysize) {
                var location = Pair(x, y)
                allmap.put(location, BoomItem(x, y, false, index).apply { isshow = false;local = location })
                index++;
            }
        }
        while (boommap.size < currentlevel.boommax) {
            var bx: Int = (Math.random() * (xsize + 1)).toInt()
            var by = (Math.random() * (ysize + 1)).toInt()
            var key = Pair(bx, by)
            if (!boommap.contains(key)) {
                allmap.get(key)?.isBoom = true
                boommap.put(key, allmap.get(key))
            }
        }
    }

    /**
     * 计算有雷附近8个按钮的雷数
     */
    fun Countboom() {
        for (value in boommap) {
            var x = value.key.first
            var y = value.key.second
            with(Pair(x - 1, y - 1)) {
                if (allmap.contains(this) && allmap.get(this)?.roundcount == 0) {
                    allmap[this]?.roundcount = getRoundBoom(x - 1, y - 1)
                }
            }
            with(Pair(x, y - 1)) {
                if (allmap.contains(this) && allmap.get(this)?.roundcount == 0) {
                    allmap[this]?.roundcount = getRoundBoom(x, y - 1)
                }
            }
            with(Pair(x + 1, y - 1)) {
                if (allmap.contains(this) && allmap.get(this)?.roundcount == 0) {
                    allmap[this]?.roundcount = getRoundBoom(x + 1, y - 1)
                }
            }
            with(Pair(x - 1, y)) {
                if (allmap.contains(this) && allmap.get(this)?.roundcount == 0) {
                    allmap[this]?.roundcount = getRoundBoom(x - 1, y)
                }
            }
            with(Pair(x + 1, y)) {
                if (allmap.contains(this) && allmap.get(this)?.roundcount == 0) {
                    allmap[this]?.roundcount = getRoundBoom(x + 1, y)
                }
            }
            with(Pair(x - 1, y + 1)) {
                if (allmap.contains(this) && allmap.get(this)?.roundcount == 0) {
                    allmap[this]?.roundcount = getRoundBoom(x - 1, y + 1)
                }
            }
            with(Pair(x, y + 1)) {
                if (allmap.contains(this) && allmap.get(this)?.roundcount == 0) {
                    allmap[this]?.roundcount = getRoundBoom(x, y + 1)
                }
            }
            with(Pair(x + 1, y + 1)) {
                if (allmap.contains(this) && allmap.get(this)?.roundcount == 0) {
                    allmap[this]?.roundcount = getRoundBoom(x + 1, y + 1)
                }
            }
        }
    }

    fun getXYCount(): Pair<Int, Int> {
        var x = 0;
        var y = 0;
        x = currentlevel.xcount
        y = currentlevel.ycount
        return Pair(x, y);
    }

    fun getBoomData(): List<BoomItem>? {
        if (boomdata == null) initBoomData()
        return boomdata
    }

    fun reset() {
        allmap.clear();allmap == null
        boommap.clear();boomdata?.clear();boomdata = null
        checkarray.clear();checkedarray.clear()
        isGameOver = false
    }

    fun initBoomData() {
        getrandomBoom();
        Countboom()
        boomdata = ArrayList<BoomItem>().apply { allmap.values.forEach { add(it) }; }
    }

    /**
     * 将一个点周围存在的点全推入栈
     */
    fun pullPoint(point: BoomItem) {
        getRoundPoint(point.local).forEach {
            if (!checkedarray.contains(allmap.get(it))) {
                if (!checkarray.contains(allmap.get(it)) && !checkedarray.contains(allmap.get(it)))//只有当两边都没有他的时候加进去
                {   Logs.e("增加检查项","-"+allmap.get(it)?.x+"---"+allmap.get(it)?.y)
                    checkarray.offer(allmap.get(it))
                }
//                allmap.get(it)?.isshow = true
            } else {
                Logs.e("检查过了", "" + it.first + "--" + it.second)
            }
        }
    }


    fun CheckBoom(boom: BoomItem, adapter: BoomAdapter): Boolean {
        boom.isshow = true //
        if (boom.isBoom) {
            boom.isclick = true
            isGameOver = true
            return true
        }
        checkarray.offer(allmap.get(boom.local))//请注意hash值 尽量用统一数据源
        while (checkarray.peek() != null) {//队列中还有值时候继续
            var temp = checkarray.poll()
            checkpoint(temp)//考虑到
            if (adapter !== null) adapter.notifyItemChanged(temp.index)
        }
        isGameOver = checkOver()
        return false
    }

    /**
     * 校验该模块是否是雷区
     */
    fun CheckBoom(boom: BoomItem): Boolean {
        boom.isshow = true //
        if (boom.isBoom) {
            boom.isclick = true
            isGameOver = true;
            return true;
        }
        checkarray.offer(allmap.get(boom.local))//请注意hash值 尽量用统一数据源
        while (checkarray.peek() != null) {//队列中还有值时候继续
            var temp = checkarray.poll()
            checkpoint(temp)
        }
        isGameOver = checkOver()
        return false;
    }

    /**
     * 将所有雷翻出来
     */
    fun showAllBoom(adapter: BoomAdapter?) {
        boommap.values.forEach { it.isshow = true;if (adapter != null) adapter.notifyItemChanged(it.index) }

    }

    /**
     * 检测某个点是否是可以展开周围八个点,如果为0则将周围点加入队列
     */
    fun checkpoint(point: BoomItem) {
        if (point != null) {
            point?.isshow = true//翻开
            checkedarray.offer(allmap.get(point.local))
            Logs.e("检查过" + "" + point.x + "------" + point.y, "当前已经检查过的数据大小" + checkedarray.size)
            if (getRoundBoom(point.local.first, point.local.second) == 0) {//安全点
                Logs.e("周围为空", "准备扩散")
                pullPoint(point)
            }
        }
    }

    /**
     * 检验是否完成游戏
     */
    fun checkOver(): Boolean = with(boommap) {
        for (item in values) {
            if (item.isshow == true) return true
        }
        if (checkedarray.size == (currentlevel.xcount * currentlevel.ycount - currentlevel.boommax)) return true//完成全部构建
        false
    }

    fun getRoundPoint(point: Pair<Int, Int>): List<Pair<Int, Int>> {
        return with(point) {//考虑到和adapter的索引对应 这里的xy值是反的 不是xy轴
            listOf(Pair(first - 1, second - 1), Pair(first-1, second ), Pair(first - 1, second + 1),
                    Pair(first, second - 1), Pair(first, second + 1)
                    , Pair(first + 1, second - 1), Pair(first + 1, second), Pair(first + 1, second + 1))
                    .filter { it.first >= 0 && it.second >= 0 && it.first < currentlevel.xcount && it.second < currentlevel.ycount }
        }
    }

    /**
     * 单纯地计算有几个雷
     */
    fun getRoundBoom(x: Int, y: Int): Int {
        var count = 0
        //  getRoundPoint(Pair(x,y)).forEach { if(allmap.get(it)?.isBoom?:false) count++   }
        count = with(Pair(x, y)) {
            var sum = 0
            if (allmap.get(Pair(x - 1, y - 1))?.isBoom ?: false) sum++ //左上
            if (allmap.get(Pair(x, y - 1))?.isBoom ?: false) sum++//中上
            if (allmap.get(Pair(x + 1, y - 1))?.isBoom ?: false) sum++//右上
            if (allmap.get(Pair(x - 1, y))?.isBoom ?: false) sum++//中左
            if (allmap.get(Pair(x + 1, y))?.isBoom ?: false) sum++//中右
            if (allmap.get(Pair(x - 1, y + 1))?.isBoom ?: false) sum++//下左
            if (allmap.get(Pair(x, y + 1))?.isBoom ?: false) sum++//下中
            if (allmap.get(Pair(x + 1, y + 1))?.isBoom ?: false) sum++//下右
            sum
        }
        return count
    }

    enum class Levels(val boommax: Int, val xcount: Int, val ycount: Int) {
        VERYEASY(3, 5, 5), EASY(10, 8, 8), NORMAL(20, 10, 10), HARD(30, 15, 15)
    }

    fun isOver() = isGameOver

}

