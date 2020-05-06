package com.gfz.mvp.ui.activity

import androidx.recyclerview.widget.GridLayoutManager
import com.gfz.mvp.R
import com.gfz.mvp.adapter.TestCalendarAdapter
import com.gfz.mvp.base.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.concurrent.ConcurrentHashMap

/**
 * created by gfz on 2020/4/6
 **/
class TestCalendarActivity: BaseActivity() {
    private val adapter = TestCalendarAdapter("2015-04-03","2025-04-05")
    override fun getLayoutId(): Int {
        return R.layout.activity_calendar
    }

    override fun initView() {
        rv_calendar.layoutManager = GridLayoutManager(this,7)
        rv_calendar.adapter = adapter
        tv_pre.setOnClickListener {
            adapter.preMonth()
            tv_now.text = adapter.getDateTime()
        }
        tv_next.setOnClickListener {
            adapter.laterMonth()
            tv_now.text = adapter.getDateTime()
        }
    }

    override fun initData() {
        adapter.show()
        tv_now.text = adapter.getDateTime()

    }
}