package com.gfz.lab.ui.fragment.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.lab.R
import com.gfz.lab.adapter.TestMultipleChooseAdapter
import com.gfz.lab.databinding.FragmentMultiChooseBinding
import com.gfz.lab.model.bean.MultipleChooseBean
import com.gfz.ui.base.page.BaseVBFragment
import java.util.*

/**
 * created by gaofengze on 2020/4/14
 */
class TestMultipleChooseFragment : BaseVBFragment<FragmentMultiChooseBinding>() {

    override fun initView() {
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = TestMultipleChooseAdapter(
            listOf(
                MultipleChooseBean("这是第一条", 1),
                MultipleChooseBean("这是第二条", 1),
                MultipleChooseBean("这是第三条", 2),
                MultipleChooseBean("这是第四条", 3),
                MultipleChooseBean("这是第五条", 4),
                MultipleChooseBean("这是第六条", 4),
                MultipleChooseBean("这是第七条", 4),
                MultipleChooseBean("这是第八条", 5),
                MultipleChooseBean("这是第九条", 5),
                MultipleChooseBean("这是第十条", 5),
                MultipleChooseBean("这是第十一条", 5),
                MultipleChooseBean("这是第十二条", 6),
                MultipleChooseBean("这是第一条", 6),
                MultipleChooseBean("这是第二条", 6),
            )
        )
        binding.rvList.adapter = adapter
    }

    override fun getTitleText(): String {
        return "滑动多选试验区"
    }
}