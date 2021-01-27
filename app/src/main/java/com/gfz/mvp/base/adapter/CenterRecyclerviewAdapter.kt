package com.gfz.mvp.base.adapter

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * created by gaofengze on 2021/1/27
 */

/**
 * 可以滚动到中间的适配器
 * created by gfz on 2019-12-30
 */
abstract class CenterRecyclerviewAdapter<T> @JvmOverloads constructor(
    context: Context,
    needStopCenter: Boolean = false
) : BaseRecyclerViewAdapter<T>() {
    private lateinit var manager: LinearLayoutManager
    private var recyclerView: RecyclerView? = null
    private val mScroller: CenterScroller
    private val mQuickScroller: CenterScroller
    // 控制recyclerview滑动结束后,一定有一个item停在屏幕正中央
    private var snapHelper: PagerSnapHelper? = null
    private var onItemChangeListener: OnItemChangeListener? = null
    private val onScrollListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var position = 0
                    if (snapHelper != null) {
                        snapHelper!!.findSnapView(manager)?.apply {
                            position = manager.getPosition(this)
                        }
                    } else {
                        position = manager.findFirstCompletelyVisibleItemPosition()
                    }
                    if (firstVisibleItem != position) {
                        onItemChange(position)
                    }
                }
            }
        }
    }
    private var firstVisibleItem = -1
    private var needChangeIndexAfterMoveEvent: Boolean

    init {
        mScroller = CenterScroller(context, 50f)
        mQuickScroller = CenterScroller(context, 1f)
        if (needStopCenter) {
            snapHelper = PagerSnapHelper()
        }
        needChangeIndexAfterMoveEvent = false
    }

    interface OnItemChangeListener {
        fun onItemChange(position: Int)
    }

    fun setOnItemChangeListener(onItemChangeListener: CenterRecyclerviewAdapter.OnItemChangeListener?) {
        this.onItemChangeListener = onItemChangeListener
    }

    fun setNeedChangeIndexAfterMoveEvent(needChangeIndexAfterMoveEvent: Boolean) {
        this.needChangeIndexAfterMoveEvent = needChangeIndexAfterMoveEvent
    }

    open fun onItemChange(position: Int) {
        firstVisibleItem = position
        onItemChangeListener?.onItemChange(position)
        if (needChangeIndexAfterMoveEvent) {
            setClickIndex(position)
        }
    }

    fun smoothScrollToPosition(position: Int) {
        if (isItemIndex(position)) {
            mScroller.targetPosition = position
            manager.startSmoothScroll(mScroller)
            if (!needChangeIndexAfterMoveEvent) {
                setClickIndex(position)
            }
        }
    }

    /**
     * 使item滚动到屏幕中间，需要提供item的宽度
     */
    fun scrollToPosition(position: Int, offset: Int = 0) {
        if (isItemIndex(position)) {
            if (offset == 0){
                recyclerView?.scrollToPosition(position)
            }else{
                manager.scrollToPositionWithOffset(position, offset)
            }
            recyclerView?.post {
                mQuickScroller.targetPosition = position
                manager.startSmoothScroll(mQuickScroller)
                if (!needChangeIndexAfterMoveEvent) {
                    setClickIndex(position)
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        manager = recyclerView.layoutManager as LinearLayoutManager
        this.recyclerView = recyclerView
        recyclerView.removeOnScrollListener(onScrollListener)
        recyclerView.addOnScrollListener(onScrollListener)
        snapHelper?.attachToRecyclerView(recyclerView)
    }

    internal class CenterScroller(context: Context, private val time: Float) :
        LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int {
            return (boxStart + boxEnd) / 2 - (viewStart + viewEnd) / 2
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return time / displayMetrics.densityDpi
        }
    }
}