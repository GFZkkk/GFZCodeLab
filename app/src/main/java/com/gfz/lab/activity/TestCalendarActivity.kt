package com.gfz.lab.activity

import androidx.recyclerview.widget.GridLayoutManager
import com.gfz.lab.adapter.TestCalendarAdapter
import com.gfz.lab.base.BaseActivity
import com.gfz.lab.databinding.ActivityCalendarBinding
import com.gfz.lab.utils.viewBind

/**
 * created by gfz on 2020/4/6
 **/
class TestCalendarActivity: BaseActivity() {

    private val binding: ActivityCalendarBinding by viewBind()

    private val adapter = TestCalendarAdapter("2019-04-05","2020-06-05")

    override fun initView() {
        with(binding){
            rvCalendar.layoutManager = GridLayoutManager(getContext(),7)
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