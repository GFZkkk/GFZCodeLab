package com.gfz.mvp.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.gfz.mvp.R
import com.gfz.mvp.base.adapter.BaseCalendarAdapter
import com.gfz.mvp.base.adapter.BaseRecyclerViewHolder
import com.gfz.mvp.databinding.ItemCalendarBinding
import com.gfz.mvp.model.bean.BaseCalendarBean
import com.gfz.mvp.utils.viewBind

/**
 * created by gfz on 2020/4/6
 **/
class TestCalendarAdapter(sDate: String, eDate: String):
    BaseCalendarAdapter<BaseCalendarBean>(sDate, eDate){

    override fun getCalendarBean(year: Int, month: Int, day: Int): BaseCalendarBean {
        return BaseCalendarBean(intArrayOf(year,month,day))
    }

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<BaseCalendarBean> {
        return ViewHolder(viewBind(parent))
    }

    inner class ViewHolder(private val binding: ItemCalendarBinding): BaseRecyclerViewHolder<BaseCalendarBean>(binding){
        override fun onBindViewHolder(data: BaseCalendarBean, position: Int) {
            binding.tvDate.text = data.date.getDay().toString()
        }

    }

}