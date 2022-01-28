package com.gfz.lab.ui.fragment.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.lab.adapter.TestMultipleChooseAdapter
import com.gfz.lab.databinding.FragmentMultiChooseBinding
import com.gfz.lab.model.bean.MultipleChooseBean
import com.gfz.lab.base.BaseVBFragment
import java.util.*


/**
 * created by gaofengze on 2020/4/14
 */

class TestMultipleChooseFragment : BaseVBFragment<FragmentMultiChooseBinding>() {

    override fun initView() {
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = TestMultipleChooseAdapter()

        adapter.addAll(object : ArrayList<MultipleChooseBean>() {
            init {
                add(MultipleChooseBean("这是第一条", 1))
                add(MultipleChooseBean("这是第二条", 1))
                add(MultipleChooseBean("这是第三条", 2))
                add(MultipleChooseBean("这是第四条", 3))
                add(MultipleChooseBean("这是第五条", 4))
                add(MultipleChooseBean("这是第六条", 4))
                add(MultipleChooseBean("这是第七条", 4))
                add(MultipleChooseBean("这是第八条", 5))
                add(MultipleChooseBean("这是第九条", 5))
                add(MultipleChooseBean("这是第十条", 5))
                add(MultipleChooseBean("这是第十一条", 5))
                add(MultipleChooseBean("这是第十二条", 6))
                add(MultipleChooseBean("这是第一条", 6))
                add(MultipleChooseBean("这是第二条", 6))
            }
        })

        binding.rvList.adapter = adapter
    }

    override fun getTitleText(): String {
        return "滑动多选试验区"
    }
}