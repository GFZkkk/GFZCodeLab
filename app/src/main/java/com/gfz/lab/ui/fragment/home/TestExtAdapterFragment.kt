package com.gfz.lab.ui.fragment.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.lab.adapter.TestExtAdapter
import com.gfz.lab.databinding.FragmentExtlayoutBinding
import com.gfz.lab.databinding.LayoutHeaderTestBinding
import com.gfz.lab.ui.base.BaseVBFragment
import com.gfz.lab.utils.viewBind
import java.util.*

/**
 *
 * created by gaofengze on 2021/2/22
 */
class TestExtAdapterFragment : BaseVBFragment<FragmentExtlayoutBinding>(){

    override fun initView() {

        with(binding) {

            val adapter = TestExtAdapter()
            rvList.adapter = adapter
            rvList.layoutManager = LinearLayoutManager(requireContext())
            adapter.refresh(listOf(1,2,3,4))
            val headerBinding: LayoutHeaderTestBinding = viewBind(rvList)

            adapter.headerViewBinding = headerBinding
            headerBinding.tvHeader.text = "测试"

        }
    }
}