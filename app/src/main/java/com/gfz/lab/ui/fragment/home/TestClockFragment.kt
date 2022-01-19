package com.gfz.lab.ui.fragment.home

import android.media.MediaPlayer
import android.util.SparseArray
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gfz.lab.adapter.TestClockAdapter
import com.gfz.recyclerview.decoration.NormalDecoration
import com.gfz.recyclerview.adapter.BaseCenterAdapter
import com.gfz.lab.databinding.FragmentClockBinding
import com.gfz.lab.ui.base.BaseVBFragment
import com.gfz.common.utils.TopLog
import com.gfz.common.ext.toPX
import java.util.ArrayList
import kotlin.math.abs

/**
 * Created by gaofengze on 2020/7/2.
 */
class TestClockFragment : BaseVBFragment<FragmentClockBinding>() {

    val adapter by lazy {
        TestClockAdapter(requireContext())
    }
    private var curIndex = 0
    private var chooseTime: Int? = 5

    override fun initView() {
        binding.rvList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvList.addItemDecoration(NormalDecoration(0, 90.toPX()))
        binding.rvList.adapter = adapter
        val timeItems: MutableList<Int?> = ArrayList()
        for (i in 1..12) {
            timeItems.add(i * 5)
        }
        adapter.refresh(timeItems)

        adapter.setOnItemChangeListener(object : BaseCenterAdapter.OnItemChangeListener {
            override fun onItemChange(position: Int) {
                binding.tvClock.text = String.format("%s:00", adapter.getData(position))
                chooseTime = adapter.getData(position)
            }
        })

        adapter.setOnItemScrollListener(object : BaseCenterAdapter.OnItemScrollListener {
            override fun onItemScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val index: Int = getIndexByOffset(position, positionOffset)
                if (index != -1 && index != curIndex) {
                    curIndex = index
//                    play(curIndex, context, R.raw.keyboard, null)
                }
            }
        })

        val position = chooseTime!! / 5 - 1
        adapter.scrollToPosition(position)
        binding.tvClock.text = String.format("%s:00", adapter.getData(position))
    }

    override fun getTitleText(): String {
        return "时钟试验区"
    }

    private fun getIndexByOffset(position: Int, positionOffset: Float): Int {
        val offset = abs((positionOffset * 100).toInt())
        val x = 5
        val a = intArrayOf(0, 20, 40, 60, 80)
        val range = 100
        var r = offset + x
        var r1 = offset - x
        if (r >= range) {
            r = 0
        }
        if (r1 < 0) {
            r1 = 0
        }
        for (j in a.indices) {
            if (a[j] in r1..r) {
                return j
            }
        }
        if (curIndex != -1 && curIndex < a.size - 1) {
            if (r >= a[curIndex + 1]) {
                TopLog.e(String.format("拯救：%s", curIndex + 1))
                return curIndex + 1
            }
        }
        return -1
    }

    var mediaPlayerList = SparseArray<MediaPlayer>()

    /**
     * 通过播放器播放资源音频文件
     */
    /*fun play(index: Int, context: Context?, resId: Int, callback: PlayWorldCallback?) {
        if (fastClick(1, 16)) {
            return
        }
        // 设置播放资源并播放
        try {
            releaseMediaPlayer(index)
            //实例化音频播放器
            mediaPlayerList.put(index, MediaPlayer.create(context, resId))
            //准备音频播放器
            initPlayer(index)
        } catch (e: Exception) {
            TopLog.e(e.message)
            if (callback != null) {
                callback.failed()
            }
        }
    }*/
}