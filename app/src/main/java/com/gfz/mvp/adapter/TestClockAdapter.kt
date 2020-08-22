package com.gfz.mvp.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import com.gfz.mvp.R
import com.gfz.mvp.base.adapter.BaseRecyclerViewHolder
import com.gfz.mvp.base.adapter.CenterRecyclerviewAdapter
import kotlinx.android.synthetic.main.item_clock.view.*

/**
 * Created by gaofengze on 2020/7/2.
 */
class TestClockAdapter(context: Context? = null
): CenterRecyclerviewAdapter<String>(context, PagerSnapHelper()) {

    init {
        setLayoutId(R.layout.item_clock)
    }

    override fun getViewHolder(view: View, viewType: Int): BaseRecyclerViewHolder<String> {
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View): BaseRecyclerViewHolder<String>(view){

        override fun onBindViewHolder(data: String, position: Int) {
            itemView.v_clock1.setVisible(!isFirstData(position))
            itemView.v_clock2.setVisible(!isFirstData(position))
            itemView.v_clock4.setVisible(!isLastData(position))
            itemView.v_clock5.setVisible(!isLastData(position))
            itemView.v_left.setDisplay(isFirstData(position))
            itemView.v_right.setDisplay(isLastData(position))
            itemView.tv_clock_time.text = data
        }

    }
}