package com.gfz.mvp.activity

import androidx.recyclerview.widget.GridLayoutManager
import com.gfz.mvp.adapter.TestCalendarAdapter
import com.gfz.mvp.base.BaseActivity
import com.gfz.mvp.databinding.ActivityCalendarBinding
import com.gfz.mvp.utils.viewBind

/**
 * created by gfz on 2020/4/6
 **/
class TestCalendarActivity: BaseActivity() {

    private val binding: ActivityCalendarBinding by viewBind()

    private val adapter = TestCalendarAdapter("2019-04-05","2020-06-05")

    override fun initView() {
        with(binding){
            rvCalendar.layoutManager = GridLayoutManager(getContent(),7)
            rvCalendar.adapter = adapter
            tvPre.setOnClickListener {
                adapter.preMonth()
                tvNow.text = adapter.getDateTime()
            }
            tvNext.setOnClickListener {
                adapter.laterMonth()
                tvNow.text = adapter.getDateTime()
            }
        }

    }

    override fun initData() {
        adapter.show()
        binding.tvNow.text = adapter.getDateTime()

    }
}