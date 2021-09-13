package com.gfz.lab.ui.fragment.home

import androidx.recyclerview.widget.GridLayoutManager
import com.gfz.lab.R
import com.gfz.lab.adapter.Test3Adapter
import com.gfz.lab.ui.base.BaseFragment
import com.gfz.lab.databinding.FragmentHomeBinding
import com.gfz.lab.databinding.FragmentTabMainBinding
import com.gfz.lab.model.bean.TestBean
import com.gfz.lab.ui.base.BaseVBFragment
import com.gfz.lab.utils.TopLog
import com.gfz.lab.utils.viewBind

/**
 * 首页
 * created by gaofengze on 2021/5/14
 */
class HomeFragment : BaseVBFragment<FragmentHomeBinding>() {

    override fun initView() {
        TopLog.e("HomeFragment")
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
            TestBean("扩展布局实验区")
        ))
        adapter.setOnItemClickListener { _, position ->
            when(position){
                0 -> start(R.id.to_testCalendarFragment)
                1 -> start(R.id.to_testMultipleChooseFragment)
                2 -> start(R.id.to_testCountDownFragment)
                3 -> start(R.id.to_testClockFragment)
                4 -> start(R.id.to_testMoveFragment)
                5 -> start(R.id.to_testAnimationFragment)
                6 -> start(R.id.to_testExtAdapterFragment)
            }
        }
    }
}