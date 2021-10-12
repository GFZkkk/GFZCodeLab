package com.gfz.lab.base.recyclerview.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.Px
import androidx.recyclerview.widget.*
import com.gfz.lab.base.recyclerview.NormalDecoration
import com.gfz.lab.ext.toPX
import com.gfz.common.ScreenUtil
import com.gfz.lab.utils.TopLog

/**
 * 可以滚动到中间的适配器
 * created by gfz on 2019-12-30
 */
abstract class BaseCenterAdapter<T>(
    var context: Context,
    needStopCenter: Boolean = false,
    private var smoothTime: Float = 50f
) : BaseRecyclerViewAdapter<T>() {
    private var mLayoutManager: LinearLayoutManager? = null
    private var mRecyclerView: RecyclerView? = null

    // 控制recyclerview滑动结束后,一定有一个item停在屏幕正中央
    private var snapHelper: SnapHelper? = null

    // 该回调优先于checkIndex变化
    private var onItemChangeListener: OnItemChangeListener? = null
    private var onItemScrollListener: OnItemScrollListener? = null
    private var onScrollListener: RecyclerView.OnScrollListener? = null
    private var firstVisiblePosition = 0

    // 是否在滑动后自动更新checkIndex
    var needChangeIndexAfterMoveEvent = false
    private var offset = 0
    private val CHECK_TAG = 101
    private var headDecorations = 0
    private var targetView: View? = null

    interface OnItemChangeListener {
        fun onItemChange(position: Int)
    }

    interface OnItemScrollListener {
        fun onItemScrolled(position: Int, positionOffset: Float, @Px positionOffsetPixels: Int)
    }

    init {
        if (needStopCenter) {
            snapHelper = getSnapHelper()
        }
        offset = if (getItemWidth() != 0) {
            (ScreenUtil.getScreenWidth(context) - getItemWidth().toPX(context)) / 2
        } else {
            0
        }
    }

    fun setOnItemChangeListener(onItemChangeListener: OnItemChangeListener?) {
        this.onItemChangeListener = onItemChangeListener
    }

    open fun setOnItemScrollListener(onItemScrollListener: OnItemScrollListener?) {
        this.onItemScrollListener = onItemScrollListener
    }

    fun setHeadDecorations(headDecorations: Int) {
        this.headDecorations = headDecorations.toPX()
    }

    open fun getItemWidth(): Int {
        return 0
    }

    protected open fun getSnapHelper(): SnapHelper {
        return PagerSnapHelper()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
        mRecyclerView = recyclerView
        if (mLayoutManager != null) {
            recyclerView.removeOnScrollListener(getOnScrollListener()!!)
            recyclerView.addOnScrollListener(getOnScrollListener()!!)
        } else {
            TopLog.i("LayoutManager为空，设置OnScrollListener失败")
        }
        if (snapHelper != null) {
            recyclerView.onFlingListener = null
            snapHelper!!.attachToRecyclerView(recyclerView)
        }
        if (recyclerView.itemDecorationCount > 0) {
            val itemDecoration = recyclerView.getItemDecorationAt(0)
            headDecorations = (itemDecoration as? NormalDecoration)?.headMargin ?: 0
        }
    }

    //需要避免检查过于频繁
    protected open fun updatePosition() {
        if (timeCell.overTime(CHECK_TAG, 10)) {
            checkPosition()
        }
    }

    private fun checkPosition() {
        timeCell.start(CHECK_TAG)
        var position = 0
        if (snapHelper != null) {
            val item = snapHelper!!.findSnapView(mLayoutManager)
            if (item != null) {
                position = mLayoutManager!!.getPosition(item)
            }
            if (onScrollListener != null) {
                targetView = item
            }
        } else {
            position = mLayoutManager!!.findFirstCompletelyVisibleItemPosition()
            if (onScrollListener != null) {
                targetView = mLayoutManager!!.findViewByPosition(position)
            }
        }
        // 通知item变动
        if (firstVisiblePosition != position) {
            onItemChange(position)
        }
    }

    private fun updateScrolledInfo() {
        // 更新滚动信息
        updateScrolledInfo(targetView)
    }

    private fun updateScrolledInfo(firstVisibleView: View?) {
        if (onItemScrollListener != null) {
            if (firstVisiblePosition == RecyclerView.NO_POSITION) {
                onItemScrollListener!!.onItemScrolled(0, 0f, 0)
                return
            }
            if (firstVisibleView == null) {
                onItemScrollListener!!.onItemScrolled(0, 0f, 0)
                return
            }
            var leftDecorations = mLayoutManager!!.getLeftDecorationWidth(firstVisibleView)
            var rightDecorations = mLayoutManager!!.getRightDecorationWidth(firstVisibleView)
            var topDecorations = mLayoutManager!!.getTopDecorationHeight(firstVisibleView)
            var bottomDecorations = mLayoutManager!!.getBottomDecorationHeight(firstVisibleView)
            val params = firstVisibleView.layoutParams
            if (params is MarginLayoutParams) {
                val margin = params
                leftDecorations += margin.leftMargin
                rightDecorations += margin.rightMargin
                topDecorations += margin.topMargin
                bottomDecorations += margin.bottomMargin
            }
            val decoratedHeight = firstVisibleView.height + topDecorations + bottomDecorations
            val decoratedWidth = firstVisibleView.width + leftDecorations + rightDecorations
            val isHorizontal = mLayoutManager!!.orientation == LinearLayoutManager.HORIZONTAL
            val start: Int
            val sizePx: Int
            if (isHorizontal) {
                sizePx = decoratedWidth
                start =
                    firstVisibleView.left - headDecorations - leftDecorations - mRecyclerView!!.paddingLeft
            } else {
                sizePx = decoratedHeight
                start =
                    firstVisibleView.top - headDecorations - topDecorations - mRecyclerView!!.paddingTop
            }
            val mOffsetPx = -1 * start
            val mOffset: Float = if (sizePx == 0) 0F else mOffsetPx * 100 / sizePx / 100f
            onItemScrollListener!!.onItemScrolled(firstVisiblePosition, mOffset, mOffsetPx)
            if (mOffset > 0.99f || mOffset < -0.99f) {
                updatePosition()
            }
        }
    }

    open fun onItemChange(position: Int) {
        firstVisiblePosition = position
        if (onItemChangeListener != null) {
            onItemChangeListener!!.onItemChange(position)
        }
        if (needChangeIndexAfterMoveEvent) {
            setClickIndex(position)
        }
    }

    open fun smoothScrollToPosition(position: Int) {
        if (isItemIndex(position) && mLayoutManager != null) {
            mRecyclerView!!.post {
                mLayoutManager!!.startSmoothScroll(getScroller(position))
                if (!needChangeIndexAfterMoveEvent) {
                    setClickIndex(position)
                }
            }
        }
    }

    open fun scrollToPosition(position: Int) {
        if (isItemIndex(position) && mRecyclerView != null && mLayoutManager != null) {
            if (offset > 0) {
                mLayoutManager!!.scrollToPositionWithOffset(position, offset)
            } else {
                mLayoutManager!!.scrollToPosition(position)
            }
            checkPosition()
            mRecyclerView!!.post {
                mLayoutManager!!.startSmoothScroll(getQuickScroller(position))
                setClickIndex(position)
            }
        }
    }

    open fun <VH : BaseRecyclerViewHolder<T>?> getHolderByPosition(position: Int): VH? {
        if (isItemIndex(position)) {
            return mLayoutManager?.findViewByPosition(position)?.let {
                mRecyclerView?.getChildViewHolder(it) as? VH
            }
        }
        return null
    }

    fun getOnScrollListener(): RecyclerView.OnScrollListener? {
        if (onScrollListener == null) {
            onScrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        checkPosition()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    updateScrolledInfo()
                }
            }
        }
        return onScrollListener
    }

    protected open fun getScroller(position: Int): CenterScroller? {
        val centerScroller = CenterScroller(context, smoothTime)
        centerScroller.targetPosition = position
        return centerScroller
    }

    fun getQuickScroller(position: Int): CenterScroller? {
        val centerScroller = CenterScroller(context, 1f)
        centerScroller.targetPosition = position
        return centerScroller
    }

    class CenterScroller(context: Context?, private val time: Float) :
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