package com.gfz.recyclerview.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * 轮播图
 * created by gaofengze on 2021/1/27
 */
abstract class BaseBannerAdapter<T>(context: Context, private val time: Int, private var smoothTime: Float = 50f) :
    BaseCenterAdapter<T>(context, true, smoothTime) {

    private val mHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    private var isEnd = false
    private var bannerNum = 0
    private var action = 0
    protected var filterUserActions = false

    init {
        needChangeIndexAfterMoveEvent = true
    }

    override fun onItemChange(position: Int) {
        super.onItemChange(position)
        checkBoundsNext()
        addMoveEvent()
    }

    fun readyData(data: List<T?>) {
        notifyDataAllChange{
            bannerNum = data.size
            clear()
            addData(data[data.size - 1])
            addAllData(data)
            addData(data[0])
        }
    }

    fun getBannerNum(): Int {
        return bannerNum
    }

    override fun setOnItemScrollListener(onItemScrollListener: OnItemScrollListener?) {
        if (onItemScrollListener != null) {
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
        } else {
            super.setOnItemScrollListener(null)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        addTouchEvent(recyclerView)
    }

    private fun addTouchEvent(recyclerView: RecyclerView){
        recyclerView.setOnTouchListener { _, event ->
            if (filterUserActions) {
                return@setOnTouchListener true
            }
            action = event.action
            when (action) {
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
     * 动起来
     */
    operator fun next() {
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
            scrollToPosition(1)
        } else if (nowIndex == 0) {
            scrollToPosition(bannerNum)
        }
    }

    private fun getBannerIndex(position: Int): Int {
        return when {
            isFirstData(position) -> {
                bannerNum - 1
            }
            isLastData(position) -> {
                0
            }
            else -> {
                position - 1
            }
        }
    }

    var scrollToNext = Runnable { next() }
}