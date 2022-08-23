package com.gfz.lab.ui.fragment.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.lab.adapter.Test3Adapter
import com.gfz.lab.databinding.FragmentCustomBinding
import com.gfz.lab.model.bean.TestBean
import com.gfz.ui.base.page.BaseVBFragment

/**
 * Created by gaofengze on 2020/7/2.
 */
class TestCustomFragment : BaseVBFragment<FragmentCustomBinding>(){

    private val adapter by lazy {
        Test3Adapter()
    }

    override fun getTitleText(): String {
        return "折叠实验区"
    }

    override fun initView() {
        val list = ArrayList<TestBean>().apply {
            repeat(100){
                add(TestBean("测试$it", 0))
            }
        }
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = adapter
        adapter.refresh(list)
    }


}