package com.gfz.mvp.ui.activity

import android.content.Intent
import androidx.recyclerview.widget.*
import com.gfz.mvp.R
import com.gfz.mvp.adapter.Test3Adapter
import com.gfz.mvp.base.mvp.BaseActivity
import com.gfz.mvp.model.bean.TestBean
import com.gfz.mvp.utils.TopLog
import com.gfz.mvp.utils.getmColor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        tvText.setOnClickListener {
            startActivity(Intent(this,TestCalendarActivity::class.java))
        }
        tvMultiChoose.setOnClickListener {
            startActivity(Intent(this,TestMultiChooseActivity::class.java))
        }
    }

    override fun initData() {
        val adapter = Test3Adapter()
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        tvText.setTextColor(getmColor(R.color.col_ff7d7d))
        adapter.refresh(listOf(TestBean("aa"),TestBean("bb"),TestBean("cc"),TestBean("dd"),TestBean("ee")))
        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(rvList)
        TopLog.e("测试")
    }


}
