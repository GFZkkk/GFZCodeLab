package com.gfz.mvp.ui.activity

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.mvp.R
import com.gfz.mvp.adapter.Test3Adapter
import com.gfz.mvp.base.BaseActivity
import com.gfz.mvp.callback.OnItemClickListener
import com.gfz.mvp.model.bean.TestBean
import kotlinx.android.synthetic.main.activity_main.*

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
        adapter.refresh(listOf(TestBean("cc")))
        adapter.setOnItemClickListener(OnItemClickListener { v, position ->
            when(position){
                0 -> startActivity(Intent(this, TestCalendarActivity::class.java))
            }
        })
    }


}
