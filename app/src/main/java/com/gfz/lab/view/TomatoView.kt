package com.gfz.lab.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gfz.common.ext.toPX
import com.gfz.common.utils.TopLog
import com.gfz.lab.adapter.TestClockAdapter
import com.gfz.lab.databinding.LayoutTomatoBinding
import com.gfz.recyclerview.adapter.BaseCenterAdapter
import com.gfz.recyclerview.decoration.SpaceItemDecoration
import kotlin.math.abs

/**
 *
 * created by xueya on 2022/8/22
 */
class TomatoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val adapter by lazy {
        TestClockAdapter(context)
    }
    private var curIndex = 0
    private var chooseTime: Int? = 5

    private val binding = LayoutTomatoBinding.inflate(LayoutInflater.from(context), this, true)


    init {
        with(binding) {
            rvList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rvList.addItemDecoration(
                SpaceItemDecoration(
                marginH = 90.toPX()
            )
            )
            rvList.adapter = adapter
            val timeItems: MutableList<Int?> = ArrayList()
            for (i in 1..12) {
                timeItems.add(i * 5)
            }
            adapter.refresh(timeItems)

            adapter.setOnItemChangeListener(object : BaseCenterAdapter.OnItemChangeListener {
                override fun onItemChange(position: Int) {
                    tvClock.text = String.format("%s:00", adapter.getData(position))
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
            tvClock.text = String.format("%s:00", adapter.getData(position))
        }
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
}