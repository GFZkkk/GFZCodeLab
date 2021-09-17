package com.gfz.lab.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller.ScrollVectorProvider
import androidx.recyclerview.widget.SnapHelper
import com.gfz.lab.base.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.lab.base.recyclerview.adapter.BaseCenterAdapter
import com.gfz.lab.base.recyclerview.adapter.BaseVBRecyclerViewHolder
import com.gfz.lab.databinding.ItemClockBinding
import com.gfz.lab.ext.setVisible
import kotlin.math.abs

/**
 * Created by gaofengze on 2020/7/2.
 */
class TestClockAdapter(context: Context)
    : BaseCenterAdapter<Int>(context, true) {

    override fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<Int> {
        return ViewHolder(ItemClockBinding.inflate(layoutInflater, parent, false))
    }

    inner class ViewHolder(binding: ItemClockBinding) :
        BaseVBRecyclerViewHolder<Int, ItemClockBinding>(binding) {
        override fun onBindViewHolder(data: Int, position: Int) {
            binding.vClock1.setVisible(!isFirstData(position))
            binding.vClock2.setVisible(!isFirstData(position))
            binding.vClock4.setVisible(!isLastData(position))
            binding.vClock5.setVisible(!isLastData(position))
            binding.tvClockTime.text = data.toString()
//            binding.tvClockTime.setTypeface(CommonUtil.getTypeFace(getContext(), "BebasNeue-1.otf"))
        }
    }

    override fun getSnapHelper(): SnapHelper {
        return MySnapHelper(context)
    }

    internal class MySnapHelper(val context: Context) : PagerSnapHelper() {
        override fun createScroller(layoutManager: RecyclerView.LayoutManager): SmoothScroller? {
            return if (layoutManager !is ScrollVectorProvider) {
                null
            } else object : LinearSmoothScroller(context) {
                override fun onTargetFound(
                    targetView: View,
                    state: RecyclerView.State,
                    action: Action
                ) {
                    calculateDistanceToFinalSnap(
                        layoutManager,
                        targetView
                    )?.apply {
                        val dx = this[0]
                        val dy = this[1]
                        val time = calculateTimeForDeceleration(abs(dx).coerceAtLeast(abs(dy)))
                        if (time > 0) {
                            action.update(dx, dy, time, mDecelerateInterpolator)
                        }
                    }

                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return 500f / displayMetrics.densityDpi
                }
            }
        }
    }



}