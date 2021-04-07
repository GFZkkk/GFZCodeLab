package com.gfz.mvp.base.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.Px
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.gfz.mvp.utils.ScreenUtil
import com.gfz.mvp.utils.TimeCell
import com.gfz.mvp.utils.TopLog
import com.gfz.mvp.utils.toPX

/**
 * 可以滚动到中间的适配器
 * created by gfz on 2019-12-30
 */
abstract class BaseCenterAdapter<T> constructor(
    context: Context,
    needStopCenter: Boolean = false
) : BaseRecyclerViewAdapter<T>() {
    private var mLayoutManager: LinearLayoutManager? = null
    private var mRecyclerView: RecyclerView? = null
    private val mScroller: CenterScroller by lazy{
        CenterScroller(context, 50f)
    }
    private val mQuickScroller: CenterScroller by lazy {
        CenterScroller(context, 1f)
    }

    private val timeCell : TimeCell by lazy {
        TimeCell()
    }

    // 控制recyclerview滑动结束后,一定有一个item停在屏幕正中央
    private var snapHelper: PagerSnapHelper? = null
    private var mItemChangeListener: OnItemChangeListener? = null
    private var mItemScrollListener: OnItemScrollListener? = null

    private val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                checkPosition()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            mItemScrollListener?.apply {
                updateScrolledInfo(this, recyclerView)
            }

        }
    }
    private var firstVisiblePosition = -1
    private var needChangeIndexAfterMoveEvent = false
    private var offset = 0
    private val CHECK_TAG = 101

    interface OnItemChangeListener {
        fun onItemChange(position: Int)
    }

    interface OnItemScrollListener {
        fun onItemScrolled(position: Int, positionOffset: Float, @Px positionOffsetPixels: Int)
    }

    init {
        if (needStopCenter) {
            snapHelper = PagerSnapHelper()
        }
        offset = (ScreenUtil.getScreenWidth(context) - getItemWidth().toPX(context)) / 2
    }

    fun setNeedChangeIndexAfterMoveEvent(needChangeIndexAfterMoveEvent: Boolean) {
        this.needChangeIndexAfterMoveEvent = needChangeIndexAfterMoveEvent
    }

    fun setOnItemChangeListener(onItemChangeListener: OnItemChangeListener) {
        this.mItemChangeListener = onItemChangeListener
    }

    open fun setOnItemScrollListener(onItemScrollListener: OnItemScrollListener) {
        this.mItemScrollListener = onItemScrollListener
    }

    open fun onItemChange(position: Int) {
        firstVisiblePosition = position
        mItemChangeListener?.onItemChange(position)
        if (needChangeIndexAfterMoveEvent) {
            setClickIndex(position)
        }
    }

    open fun smoothScrollToPosition(position: Int) {
        if (isItemIndex(position)) {
            mLayoutManager?.apply {
                this.startSmoothScroll(mScroller)
                mScroller.targetPosition = position
                if (!needChangeIndexAfterMoveEvent) {
                    setClickIndex(position)
                }
            }
        }
    }

    open fun scrollToCenterPosition(position: Int) {
        if (mRecyclerView != null && isItemIndex(position) && mLayoutManager != null) {
            mLayoutManager?.scrollToPositionWithOffset(position, offset)
            mRecyclerView?.post {
                mQuickScroller.targetPosition = position
                mLayoutManager?.startSmoothScroll(mQuickScroller)
                if (!needChangeIndexAfterMoveEvent) {
                    setClickIndex(position)
                }
            }
        }
    }

    open fun scrollToPosition(position: Int) {
        if (isItemIndex(position)) {
            mRecyclerView?.apply {
                scrollToPosition(position)
                post {
                    mQuickScroller.targetPosition = position
                    mLayoutManager?.startSmoothScroll(mQuickScroller)
                    if (!needChangeIndexAfterMoveEvent) {
                        setClickIndex(position)
                    }
                }
            }

        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
        mRecyclerView = recyclerView
        if (mLayoutManager != null) {
            recyclerView.removeOnScrollListener(onScrollListener)
            recyclerView.addOnScrollListener(onScrollListener)
        } else {
            TopLog.i("LayoutManager为空，设置OnScrollListener失败")
        }
        snapHelper?.attachToRecyclerView(recyclerView)
    }

    private fun updateScrolledInfo(onItemScrollListener : OnItemScrollListener, recyclerView: RecyclerView) {
        mLayoutManager?.apply {
            if (firstVisiblePosition == RecyclerView.NO_POSITION) {
                onItemScrollListener.onItemScrolled(0, 0f, 0)
                return
            }
            val firstVisibleView = findViewByPosition(firstVisiblePosition)
            if (firstVisibleView == null) {
                onItemScrollListener.onItemScrolled(0, 0f, 0)
                return
            }
            var leftDecorations = getLeftDecorationWidth(firstVisibleView)
            var rightDecorations = getRightDecorationWidth(firstVisibleView)
            var topDecorations = getTopDecorationHeight(firstVisibleView)
            var bottomDecorations = getBottomDecorationHeight(firstVisibleView)
            val params = firstVisibleView.layoutParams
            if (params is MarginLayoutParams) {
                leftDecorations += params.leftMargin
                rightDecorations += params.rightMargin
                topDecorations += params.topMargin
                bottomDecorations += params.bottomMargin
            }
            val decoratedHeight = firstVisibleView.height + topDecorations + bottomDecorations
            val decoratedWidth = firstVisibleView.width + leftDecorations + rightDecorations
            val isHorizontal = mLayoutManager?.orientation == LinearLayoutManager.HORIZONTAL
            val start: Int
            val sizePx: Int
            if (isHorizontal) {
                sizePx = decoratedWidth
                start = firstVisibleView.left - leftDecorations - recyclerView.paddingLeft
            } else {
                sizePx = decoratedHeight
                start = firstVisibleView.top - topDecorations - recyclerView.paddingTop
            }
            val mOffsetPx = -start
            if (mOffsetPx < 0) {
//                TopLog.e(mOffsetPx);
            }
            val mOffset: Float = if (sizePx == 0) 0F else mOffsetPx.toFloat() / sizePx
            onItemScrollListener.onItemScrolled(firstVisiblePosition, mOffset, mOffsetPx)
        }

    }

    open fun getItemWidth(): Int {
        return 0
    }

    private fun checkPosition() {
        timeCell.start(CHECK_TAG)
        mLayoutManager?.apply {
            snapHelper?.findSnapView(mLayoutManager)?.let {
                getPosition(it)
            } ?: findFirstCompletelyVisibleItemPosition().let {
                if (firstVisiblePosition != it) {
                    onItemChange(it)
                }
            }
        }
    }

    //需要避免检查过于频繁
    protected open fun updatePosition() {
        if (timeCell.overTime(CHECK_TAG, 50)) {
            checkPosition()
        }
    }

    internal class CenterScroller(context: Context?, private val time: Float) :
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