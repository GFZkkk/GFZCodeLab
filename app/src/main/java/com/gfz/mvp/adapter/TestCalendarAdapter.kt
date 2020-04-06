package com.gfz.mvp.adapter

import android.view.View
import com.gfz.mvp.R
import com.gfz.mvp.base.adapter.BaseCalendarAdapter
import com.gfz.mvp.base.adapter.BaseRecyclerViewHolder
import com.gfz.mvp.model.bean.BaseCalendarBean
import kotlinx.android.synthetic.main.item_calendar.view.*

/**
 * created by gfz on 2020/4/6
 **/
class TestCalendarAdapter(sDate: String, eDate: String):
    BaseCalendarAdapter<BaseCalendarBean>(sDate, eDate){
    init {
        setLayoutId(R.layout.item_calendar)
    }
    override fun getCalendarBean(year: Int, month: Int, day: Int): BaseCalendarBean {
        return BaseCalendarBean(intArrayOf(year,month,day))
    }

    override fun getViewHolder(
        view: View,
        viewType: Int
    ): BaseRecyclerViewHolder<BaseCalendarBean> {
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : BaseRecyclerViewHolder<BaseCalendarBean>(view) {
        override fun onBindViewHolder(data: BaseCalendarBean?, position: Int) {
            if (data != null){
                itemView.tv_date.text = data.date.getMonth().toString()
            }else{
                itemView.tv_date.text = ""
            }
        }

    }
}