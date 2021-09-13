package com.gfz.lab.ui.fragment.home

import androidx.recyclerview.widget.GridLayoutManager
import com.gfz.lab.adapter.TestCalendarAdapter
import com.gfz.lab.databinding.FragmentCalendarBinding
import com.gfz.lab.ui.base.BaseVBFragment
import com.gfz.lab.utils.viewBind

/**
 * created by gfz on 2020/4/6
 **/
class TestCalendarFragment : BaseVBFragment<FragmentCalendarBinding>(){

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
            tvNow.text = adapter.getDateTime()
        }
        adapter.show()

    }
}