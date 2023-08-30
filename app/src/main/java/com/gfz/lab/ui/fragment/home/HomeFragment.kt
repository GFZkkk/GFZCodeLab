package com.gfz.lab.ui.fragment.home

import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.gfz.lab.R
import com.gfz.lab.adapter.Test3Adapter
import com.gfz.lab.databinding.FragmentHomeBinding
import com.gfz.lab.model.bean.TestBean
import com.gfz.ui.base.page.BaseVBFragment


/**
 * 首页
 * created by gaofengze on 2021/5/14
 */
class HomeFragment : BaseVBFragment<FragmentHomeBinding>() {

    override fun initView() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),0)

        val adapter = Test3Adapter()

        binding.rvList.adapter = adapter
        binding.rvList.layoutManager = GridLayoutManager(context, 2)

        adapter.setData(
            listOf(
                TestBean("日历实验区", R.id.testCalendarFragment),
                TestBean("滑动多选实验区", R.id.testMultipleChooseFragment),
                TestBean("悬浮计时实验区", R.id.testCountDownFragment),
                TestBean("控件实验区", R.id.testCustomFragment),
                TestBean("移动列表实验区", R.id.testMoveFragment),
                TestBean("自定义动画实验区", R.id.testAnimationFragment),
                TestBean("扩展布局实验区", R.id.testExtAdapterFragment),
                TestBean("流式布局实验区", R.id.testFlowFragment),
                TestBean("画板实验区", R.id.testWhiteBoardFragment),
                TestBean("提词器实验区", R.id.testTeleprompterFragment),
            )
        )

        adapter.setOnItemClickDataListener { _, _, testBean ->
            start(testBean.idRes)
        }
    }
}