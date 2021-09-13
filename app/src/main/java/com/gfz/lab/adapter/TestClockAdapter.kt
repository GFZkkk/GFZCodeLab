package com.gfz.lab.adapter

import android.content.Context
import android.view.ViewGroup
import com.gfz.lab.base.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.lab.base.recyclerview.adapter.BaseCenterAdapter
import com.gfz.lab.databinding.ItemClockBinding
import com.gfz.lab.ext.setDisplay
import com.gfz.lab.ext.setVisible
import com.gfz.lab.utils.viewBind

/**
 * Created by gaofengze on 2020/7/2.
 */
class TestClockAdapter(context: Context)
    : BaseCenterAdapter<String>(context, true) {

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<String> {
        return ViewHolder(viewBind(parent))
    }

    inner class ViewHolder(private val binding: ItemClockBinding): BaseRecyclerViewHolder<String>(binding){

        override fun onBindViewHolder(data: String, position: Int) {
            with(binding){
                vClock1.setVisible(!isFirstData(position))
                vClock2.setVisible(!isFirstData(position))
                vClock4.setVisible(!isLastData(position))
                vClock5.setVisible(!isLastData(position))
                vLeft.setDisplay(isFirstData(position))
                vRight.setDisplay(isLastData(position))
                tvClockTime.text = data
            }

        }

    }
}