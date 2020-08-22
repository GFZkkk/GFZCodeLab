package com.gfz.mvp.base.adapter

import android.content.Context
import android.os.Vibrator
import android.util.DisplayMetrics
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.OnScrollListener

/**
 * Created by gaofengze on 2020/7/2.
 */
abstract class CenterRecyclerviewAdapter<T>(
    context: Context? = null,
    val snapHelper: SnapHelper? = PagerSnapHelper()
) : BaseRecyclerViewAdapter<T>() {

    private var manager: RecyclerView.LayoutManager? = null
    private val mScroller: CenterSmoothScroller

    // 控制recyclerview滑动结束后,一定有一个item停在屏幕正中央

    init {
        setContext(context)
        mScroller = CenterSmoothScroller(getContext())
    }

    override fun setClickIndex(clickIndex: Int) {
        if (isItemIndex(clickIndex) && clickIndex != getClickIndex() && manager != null) {
            mScroller.targetPosition = clickIndex
            manager!!.startSmoothScroll(mScroller)
        }
        super.setClickIndex(clickIndex)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        manager = recyclerView.layoutManager
        snapHelper?.attachToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener (object : OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    fun startVibrator(){

    }

    internal class CenterSmoothScroller(context: Context?) :
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
            return 25f / displayMetrics.densityDpi
        }
    }
}