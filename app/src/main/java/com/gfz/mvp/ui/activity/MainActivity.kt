package com.gfz.mvp.ui.activity

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.mvp.adapter.Test3Adapter
import com.gfz.mvp.base.BaseActivity

import com.gfz.mvp.callback.OnItemClickListener
import com.gfz.mvp.databinding.ActivityMainBinding
import com.gfz.mvp.model.bean.TestBean
import com.gfz.mvp.utils.viewBind

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by viewBind()

    override fun initData() {
        val adapter = Test3Adapter()
        binding.rvList.adapter = adapter
        binding.rvList.layoutManager = LinearLayoutManager(this)
        adapter.refresh(listOf(TestBean("日历试验区"),
            TestBean("滑动多选试验区"),
            TestBean("悬浮计时试验区"),
            TestBean("时钟试验区"),
            TestBean("移动列表实验区"),
            TestBean("自定义动画实验区")
        ))
        adapter.setOnItemClickListener(OnItemClickListener { v, position ->
            when(position){
                0 -> startActivity(Intent(this, TestCalendarActivity::class.java))
                1 -> startActivity(Intent(this,TestMultiChooseActivity::class.java))
                2 -> startActivity(Intent(this,TestCountDownActivity::class.java))
                3 -> startActivity(Intent(this,TestClockActivity::class.java))
                4 -> startActivity(Intent(this,TestMoveActivity::class.java))
                5 -> startActivity(Intent(this,TestAnimationActivity::class.java))
            }
        })
    }
}
