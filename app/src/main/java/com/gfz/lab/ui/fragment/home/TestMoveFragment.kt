package com.gfz.lab.ui.fragment.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.lab.R
import com.gfz.lab.adapter.TestMoveAdapter
import com.gfz.lab.adapter.TestSlideDeleteAdapter
import com.gfz.lab.databinding.FragmentMoveBinding
import com.gfz.lab.model.bean.MoveBean
import com.gfz.ui.base.page.BaseVBFragment

/**
 * Created by gaofengze on 2020/8/22.
 */
class TestMoveFragment : BaseVBFragment<FragmentMoveBinding>() {

    override fun initView() {
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = TestMoveAdapter(
            listOf(
                MoveBean("拼写训练"),
                MoveBean("释义训练"),
                MoveBean("例句训练"),
                MoveBean("看词训练"),
                MoveBean("听词训练"),
                MoveBean("译词训练")
            )
        )

        binding.rvList2.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList2.adapter = TestSlideDeleteAdapter()
    }

    override fun getTitleText(): String {
        return "移动列表实验区"
    }
}