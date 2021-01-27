package com.gfz.mvp.base.adapter

import android.content.Context
import android.os.Handler
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.gfz.mvp.utils.ScreenUtil
import com.gfz.mvp.utils.toPX

/**
 * 轮播图
 * created by gaofengze on 2021/1/27
 */
abstract class BannerAdapter<T>(context: Context, itemWidth: Int, private val time: Int) :
    CenterRecyclerviewAdapter<T>(context, true) {
    private var isEnd = false
    private var bannerNum = 0
    private val mHandler: Handler by lazy {
        Handler()
    }
    private val mOffset: Int

    init {
        setNeedChangeIndexAfterMoveEvent(true)
        mOffset = (ScreenUtil.getScreenWidth(context) - itemWidth.toPX()) / 2
    }

    override fun onItemChange(position: Int) {
        super.onItemChange(position)
        checkBoundsNext()
        addMoveEvent()
    }

    fun readyData(data: List<T?>) {
        bannerNum = data.size
        clear()
        addData(data[data.size - 1])
        addAllData(data)
        addData(data[0])
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    removeMoveEvent()
                }
                MotionEvent.ACTION_MOVE -> {
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    addMoveEvent()
                }
                else -> {
                }
            }
            false
        }
    }

    private fun addMoveEvent() {
        removeMoveEvent()
        if (time != 0 && !isEnd) {
            mHandler.postDelayed(scrollToNext, time.toLong())
        }
    }

    private fun removeMoveEvent() {
        if (time != 0) {
            mHandler.removeCallbacks(scrollToNext)
        }
    }

    fun start() {
        isEnd = false
        addMoveEvent()
    }

    fun end() {
        isEnd = true
        removeMoveEvent()
    }

    /**
     * 向后滚动时检查边界
     * 检测时机：用户主动滑动结束后，自动归位后触发
     */
    private fun checkBoundsNext() {
        val nowIndex = getClickIndex()
        if (nowIndex > bannerNum) {
            scrollToPosition(1, mOffset)
        } else if (nowIndex == 0) {
            scrollToPosition(bannerNum, mOffset)
        }
    }

    var scrollToNext = Runnable {
        if (itemCount != 0) {
            val next = (getClickIndex() + 1) % itemCount
            smoothScrollToPosition(next)
        }
    }
}