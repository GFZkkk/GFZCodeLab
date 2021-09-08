package com.gfz.lab.fragment

import androidx.recyclerview.widget.GridLayoutManager
import com.gfz.lab.R
import com.gfz.lab.adapter.Test3Adapter
import com.gfz.lab.base.BaseFragment
import com.gfz.lab.databinding.FragmentHomeBinding
import com.gfz.lab.model.bean.TestBean
import com.gfz.lab.utils.viewBind

/**
 * 首页
 * created by gaofengze on 2021/5/14
 */
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBind()

    override fun initView() {
        val adapter = Test3Adapter()
        binding.rvList.adapter = adapter
        binding.rvList.layoutManager = GridLayoutManager(context, 2)
        adapter.refresh(listOf(
            TestBean("日历试验区"),
            TestBean("滑动多选试验区"),
            TestBean("悬浮计时试验区"),
            TestBean("时钟试验区"),
            TestBean("移动列表实验区"),
            TestBean("自定义动画实验区"),
            TestBean("扩展布局实验区"),
            TestBean("自定义下划线试验区")
        ))
        adapter.setOnItemClickListener { _, position ->
            /*when (position) {
                0 -> startActivity(Intent(this, TestCalendarActivity::class.java))
                1 -> startActivity(Intent(this, TestMultipleChooseActivity::class.java))
                2 -> startActivity(Intent(this, TestCountDownActivity::class.java))
                3 -> startActivity(Intent(this, TestClockActivity::class.java))
                4 -> startActivity(Intent(this, TestMoveActivity::class.java))
                5 -> startActivity(Intent(this, TestAnimationActivity::class.java))
                6 -> startActivity(Intent(this, TestExtAdapterActivity::class.java))
                7 -> startActivity(Intent(this, TestUnderlineActivity::class.java))
            }*/
        }
    }
}