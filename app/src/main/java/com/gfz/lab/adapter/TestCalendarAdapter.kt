package com.gfz.lab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gfz.lab.base.recyclerview.adapter.BaseCalendarAdapter
import com.gfz.lab.base.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.lab.base.recyclerview.adapter.BaseVBRecyclerViewHolder
import com.gfz.lab.databinding.ItemCalendarBinding
import com.gfz.lab.model.bean.BaseCalendarBean
import com.gfz.lab.utils.TopLog
import com.gfz.lab.utils.viewBind
import com.google.gson.Gson

/**
 * created by gfz on 2020/4/6
 **/
class TestCalendarAdapter(sDate: String, eDate: String):
    BaseCalendarAdapter<BaseCalendarBean>(sDate, eDate){

    override fun getCalendarBean(year: Int, month: Int, day: Int): BaseCalendarBean {

        TopLog.e("$year:$month:$day")
        return BaseCalendarBean(intArrayOf(year,month,day))
    }

    override fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<BaseCalendarBean> {
        return ViewHolder(ItemCalendarBinding.inflate(layoutInflater, parent,false))
    }

    inner class ViewHolder(binding: ItemCalendarBinding): BaseVBRecyclerViewHolder<BaseCalendarBean, ItemCalendarBinding>(binding){

        override fun onBindViewHolder(data: BaseCalendarBean, position: Int) {
            TopLog.e(Gson().toJson(data))
            binding.tvDate.text = data.date.getDay().toString()
        }

    }



}