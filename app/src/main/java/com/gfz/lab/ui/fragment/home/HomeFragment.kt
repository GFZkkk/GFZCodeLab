package com.gfz.lab.ui.fragment.home

import androidx.recyclerview.widget.GridLayoutManager
import com.gfz.lab.R
import com.gfz.lab.adapter.Test3Adapter
import com.gfz.lab.databinding.FragmentHomeBinding
import com.gfz.lab.model.bean.TestBean
import com.gfz.lab.ui.base.BaseVBFragment

/**
 * 首页
 * created by gaofengze on 2021/5/14
 */
class HomeFragment : BaseVBFragment<FragmentHomeBinding>() {

    override fun initView() {

        val adapter = Test3Adapter()

        binding.rvList.adapter = adapter
        binding.rvList.layoutManager = GridLayoutManager(context, 2)

        adapter.refresh(listOf(
            TestBean("日历试验区", R.id.testCalendarFragment),
            TestBean("滑动多选试验区", R.id.testMultipleChooseFragment),
            TestBean("悬浮计时试验区", R.id.testCountDownFragment),
            TestBean("时钟试验区", R.id.testClockFragment),
            TestBean("移动列表实验区", R.id.testMoveFragment),
            TestBean("自定义动画实验区", R.id.testAnimationFragment),
//            TestBean("扩展布局实验区", R.id.testExtAdapterFragment),
            TestBean("算法实验区", R.id.testCodeFragment),
            TestBean("战场实验区", R.id.testMapFragment),
            TestBean("流式布局实验区", R.id.testFlowFragment)
        ))

        adapter.setOnItemClickDataListener { _, _, testBean ->
            testBean?.apply {
                start(idRes)
            }
        }
    }
}