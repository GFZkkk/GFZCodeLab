package com.gfz.lab.ui.fragment.func

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gfz.common.ext.getScreenWidth
import com.gfz.common.ext.toDP
import com.gfz.common.ext.toPX
import com.gfz.lab.adapter.Test3Adapter
import com.gfz.lab.adapter.TestBannerAdapter
import com.gfz.lab.databinding.FragmentFuncBinding
import com.gfz.lab.model.bean.BannerBean
import com.gfz.lab.model.bean.TestBean
import com.gfz.recyclerview.decoration.SpaceItemDecoration
import com.gfz.ui.base.page.BaseVBFragment

/**
 *
 * created by gaofengze on 2021/5/14
 */
class FuncFragment : BaseVBFragment<FragmentFuncBinding>() {
    private val adapter by lazy {
        TestBannerAdapter(requireContext())
    }

    override fun initView() {
        binding.rvList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvList.adapter = adapter
        val itemWidth = 300.toPX()
        val screenWidth = requireContext().getScreenWidth()
        val space = screenWidth - itemWidth
        val padding = space / 5
        val next = space - padding * 2
        val endPadding = padding + next
        binding.rvList.addItemDecoration(SpaceItemDecoration(
            paddingH = padding,
            marginLeft = padding,
            marginRight = endPadding,
        ))
        adapter.refresh(ArrayList<BannerBean>().apply {
            repeat(3){
                add(BannerBean("测试：$it"))
            }
        })
    }

}