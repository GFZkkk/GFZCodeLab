package com.gfz.mvp.ui.activity

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.mvp.R
import com.gfz.mvp.adapter.Test3Adapter
import com.gfz.mvp.base.BaseActivity
import com.gfz.mvp.model.bean.TestBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        tvText.setOnClickListener {
            startActivity(Intent(this,TestCalendarActivity::class.java))
        }
    }

    override fun initData() {
        val adapter = Test3Adapter()
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(this)
        tvText.setTextColor(getColor(R.color.col_ff7d7d))
        adapter.refresh(listOf(TestBean("cc")))
    }


}
