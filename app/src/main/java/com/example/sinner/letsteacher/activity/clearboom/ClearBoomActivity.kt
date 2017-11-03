package com.example.sinner.letsteacher.activity.clearboom

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.TextView

import com.example.sinner.letsteacher.R
import com.example.sinner.letsteacher.activity.BasicActivity
import com.example.sinner.letsteacher.kotterknife.*
import com.example.sinner.letsteacher.utils.DiffCallback
import com.example.sinner.letsteacher.utils.Logs
import com.example.sinner.letsteacher.views.dialog.MenuDialog
import com.pigcms.library.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_clear_boom.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class ClearBoomActivity : BasicActivity() {

    lateinit var manager: BoomManager
    lateinit var data_boom: ArrayList<BoomItem>
    var sub: Subscriber<Long>? = null;
    var currentlv = BoomManager.Levels.NORMAL;

    override fun getContentLayout() = R.layout.activity_clear_boom

    override fun initGui() {
        tv_spendtime.text = "000"
    }

    override fun initData() {
        manager = BoomManager(activity)
        initBoom()
    }

    /**
     * 初始化雷区
     */
    private fun initBoom() {
        manager.currentlevel = currentlv
        data_boom = manager.getBoomData() as ArrayList<BoomItem>? ?: ArrayList()
        rv_data_boomlayout.layoutManager = GridLayoutManager(activity, manager.currentlevel.xcount, GridLayoutManager.VERTICAL, false)
        rv_data_boomlayout.adapter = BoomAdapter(activity, data_boom, object : BoomAdapter.OnEventClick() {
            override fun onItemClick(view: View, position: Int) {
                if (!data_boom.get(position).isshow) {//只有在不是翻牌情况下才有动作
                    if (!manager.isOver()) {
                        sub ?: startcount(tv_spendtime)
                        if (manager.CheckBoom(data_boom.get(position), rv_data_boomlayout.adapter as BoomAdapter)) {
                            ToastUtil.getInstance(activity).showToast("对不起！游戏结束")
                            manager.showAllBoom(rv_data_boomlayout.adapter as BoomAdapter)
                            OverGame()
                            return
                            //rv_data_boomlayout.adapter.notifyDataSetChanged()
                        }
                        if (manager.checkOver()) {
                            OverGame()
                            ToastUtil.getInstance(activity).showToast("恭喜!游戏结束")
                            manager.showAllBoom(rv_data_boomlayout.adapter as BoomAdapter)
                            //rv_data_boomlayout.adapter.notifyDataSetChanged()
                        }
                        //本身这块逻辑 我想分开adapter 和 bean
                        //后来发现新老数据更新 每次都会有保存与遍历 并且总是在开辟新地址或参数赋值 很舍本求末
                        //如果换成了全局在高难度情况下刷新很爆炸 所以折中adapter换了一步一刷新
                        //最优逻辑是 在manager check 返回展开数组 为空就是雷 多数组就挨个一起更新
                        //rv_data_boomlayout.adapter.notifyDataSetChanged()
//                        val diffResult = DiffUtil.calculateDiff(DiffCallback(data_boom, olddatas), false)
//                        diffResult.dispatchUpdatesTo(rv_data_boomlayout.adapter)
//                        olddatas=data_boom
//                        (rv_data_boomlayout.adapter as BoomAdapter).setData(olddatas)
                    }

                }
            }
        })

    }

    /**
     * 善后工作
     */
    private fun OverGame() {

        sub?.unsubscribe()
    }

    override fun initAction() {
        tv_clear_boommenu.setOnClickListener { ShowMenu() }
    }

    /**
     * 展示菜单
     */
    private fun ShowMenu() {
        MenuDialog(activity, R.style.DialogStyle, currentlv).apply {
            setOnResultListener { i ->
                reStartGame(i)
            }
            show()
        }
    }

    private fun reStartGame(level: Int) {
        tv_spendtime.text = "000"
        sub?.unsubscribe();sub = null;
        currentlv = if (level == 0) BoomManager.Levels.VERYEASY else if (level == 1) BoomManager.Levels.EASY
        else if (level == 2) BoomManager.Levels.NORMAL else BoomManager.Levels.HARD
        manager?.reset()
        initBoom()
    }

    private

    fun startcount(tv: TextView) {
        sub = object : Subscriber<Long>() {
            override fun onError(p0: Throwable?) {
            }

            override fun onNext(p0: Long) {
                if (p0 < 1000) {
                    tv.text = "" + p0
                }
            }

            override fun onCompleted() {
            }
        }
        rx.Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sub)
    }

}
