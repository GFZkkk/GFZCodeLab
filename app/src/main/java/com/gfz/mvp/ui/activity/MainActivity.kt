package com.gfz.mvp.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.mvp.R
import com.gfz.mvp.adapter.Test3Adapter
import com.gfz.mvp.base.BaseActivity
import com.gfz.mvp.model.bean.TestBean
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_test.*

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {

    }

    override fun initData() {
        val adapter = Test3Adapter()
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(this)
        tvText.setTextColor(getColor(R.color.col_ff7d7d))
        adapter.refresh(listOf(TestBean("cc")))
    }


}
