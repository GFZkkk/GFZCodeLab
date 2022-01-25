package com.gfz.lab.ui.fragment.home

import androidx.recyclerview.widget.GridLayoutManager
import com.gfz.lab.R
import com.gfz.lab.adapter.TestCalendarAdapter
import com.gfz.lab.databinding.FragmentCalendarBinding
import com.gfz.common.ext.setColorRes
import com.gfz.common.ext.toShortDateStr
import com.gfz.lab.ui.base.BaseVBFragment
import com.gfz.common.utils.DateUtil

/**
 * created by gfz on 2020/4/6
 **/
class TestCalendarFragment : BaseVBFragment<FragmentCalendarBinding>() {

    private val adapter = TestCalendarAdapter("2020-04-06", DateUtil.addYear(1).toShortDateStr())

    override fun initView() {
        with(binding) {

            rvCalendar.layoutManager = GridLayoutManager(context, 7)
            rvCalendar.adapter = adapter

            tvPre.setOnClickListener {
                adapter.preMonth()
                tvNow.text = adapter.getDateTime()
                updateBtnVisible()
            }

            tvNext.setOnClickListener {
                adapter.laterMonth()
                tvNow.text = adapter.getDateTime()
                updateBtnVisible()
            }

            tvNow.text = adapter.getDateTime()

            adapter.show()

            updateBtnVisible()

            adapter.setOnItemClickDataListener { _, _, bean ->
                tvEvent.text = bean?.event
            }
        }


    }

    private fun updateBtnVisible() {
        binding.tvPre.setColorRes(if (adapter.havePre()) R.color.col_323640 else R.color.col_94949B)
        binding.tvNext.setColorRes(if (adapter.haveNext()) R.color.col_323640 else R.color.col_94949B)
    }
}