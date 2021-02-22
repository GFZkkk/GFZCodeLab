package com.gfz.mvp.activity

import android.app.Service
import android.os.SystemClock
import android.os.Vibrator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gfz.mvp.adapter.TestClockAdapter
import com.gfz.mvp.base.BaseActivity
import com.gfz.mvp.databinding.ActivityClockBinding
import com.gfz.mvp.utils.TopLog
import com.gfz.mvp.utils.toPX
import com.gfz.mvp.utils.viewBind
import kotlin.math.abs

/**
 * Created by gaofengze on 2020/7/2.
 */
class TestClockActivity: BaseActivity() {

    private val binding: ActivityClockBinding by viewBind()
    val adapter by lazy {
        TestClockAdapter(this)
    }

    override fun initView() {
        val vibrator: Vibrator = this.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator

        var sum = 0
        var now = 0
        binding.rvList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL,false)
        binding.rvList.adapter = adapter
        if (vibrator.hasVibrator()) {
            binding.rvList.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    sum += dx
                    if (abs(now - sum) > 12.toPX()){
                        now = sum
                        if (!fastClick()){
                            vibrator.vibrate(100)
                        }
                    }
                }
            })
        }else{
            TopLog.e("不支持震动")
        }
    }

    override fun initData() {
        val dataList: MutableList<String> = ArrayList()
        for (i in 1 .. 12){
            dataList.add((i * 5).toString())
        }
        adapter.refresh(dataList)
    }

    private var lastClickTime: Long = 0

    /**
     * 防止重复点击
     */
    private fun fastClick(): Boolean {
        val time = SystemClock.elapsedRealtime()
        return if (time - lastClickTime < 300){
            true
        }else{
            lastClickTime = time
            false
        }

    }
}