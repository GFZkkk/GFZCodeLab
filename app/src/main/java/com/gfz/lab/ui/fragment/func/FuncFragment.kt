package com.gfz.lab.ui.fragment.func

import android.view.ViewGroup
import androidx.core.view.marginTop
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
import com.google.android.material.tabs.TabLayout
import com.gyf.immersionbar.ImmersionBar

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
        binding.btnLeft.setOnClickListener {
            adapter.smoothScrollToPosition(adapter.itemCount / 2)
        }
        binding.btnRight.setOnClickListener {
            adapter.scrollToPosition(adapter.itemCount / 2)
        }
        val list = mutableListOf<String>()
        list.add("首页")
        list.add("陪练课")
        list.add("主课")
        list.add("班课")
        list.add("录播课")
        list.forEach {
            binding.tabLayout.addTab(binding.tabLayout.newTab().apply {
                text = it
            })
        }
        binding.tabLayout.apply {
            layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(0,ImmersionBar.getStatusBarHeight(this@FuncFragment),0,0)
            }
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }

}