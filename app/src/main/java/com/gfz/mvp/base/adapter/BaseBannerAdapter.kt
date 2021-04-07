package com.gfz.mvp.base.adapter

import android.content.Context
import android.os.Handler
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * 轮播图
 * created by gaofengze on 2021/1/27
 */
abstract class BaseBannerAdapter<T>(context: Context, private val time: Int) :
    BaseCenterAdapter<T>(context, true) {
    private val mHandler: Handler by lazy {
        Handler()
    }

    private var isEnd = false
    private var bannerNum = 0
    private var action = 0

    init {
        setNeedChangeIndexAfterMoveEvent(true)
    }

    override fun onItemChange(position: Int) {
        super.onItemChange(position)
        checkBoundsNext()
        addMoveEvent()
    }

    open fun readyData(data: List<T?>) {
        if (data.isEmpty()) {
            return
        }
        bannerNum = data.size
        clear()
        addData(data[data.size - 1])
        addAllData(data)
        addData(data[0])
        notifyDataSetChanged()
    }

    open fun getBannerNum(): Int {
        return bannerNum
    }

    override fun setOnItemScrollListener(onItemScrollListener: OnItemScrollListener) {
        super.setOnItemScrollListener(object : OnItemScrollListener {
            override fun onItemScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                onItemScrollListener.onItemScrolled(
                    getBannerIndex(position),
                    positionOffset,
                    positionOffsetPixels
                )
            }
        })
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.setOnTouchListener { v, event ->
            action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    removeMoveEvent()
                }
                MotionEvent.ACTION_MOVE -> {
                    updatePosition()
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

    open fun start() {
        isEnd = false
        addMoveEvent()
    }

    open fun end() {
        isEnd = true
        removeMoveEvent()
    }

    /**
     * 动起来
     */
    open operator fun next() {
        if (itemCount != 0 && action != 2) {
            val next = (getClickIndex() + 1) % itemCount
            smoothScrollToPosition(next)
        }
    }

    /**
     * 向后滚动时检查边界
     * 检测时机：用户主动滑动结束后，自动归位后触发
     */
    private fun checkBoundsNext() {
        val nowIndex = getClickIndex()
        if (nowIndex > bannerNum) {
            scrollToCenterPosition(1)
        } else if (nowIndex == 0) {
            scrollToCenterPosition(bannerNum)
        }
    }

    private fun getBannerIndex(position: Int): Int {
        return if (isFirstData(position)) {
            bannerNum - 1
        } else if (isLastData(position)) {
            0
        } else {
            position - 1
        }
    }

    var scrollToNext = Runnable { next() }
}