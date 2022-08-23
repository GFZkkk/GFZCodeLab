package com.gfz.lab.ui.fragment

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.gfz.lab.R
import com.gfz.lab.adapter.BottomBarAdapter
import com.gfz.lab.databinding.FragmentTabMainBinding
import com.gfz.lab.model.bean.TabItemBean
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.gfz.lab.ui.fragment.home.HomeFragment
import com.gfz.lab.ui.fragment.mine.MineFragment
import com.gfz.lab.ui.fragment.func.FuncFragment
import com.gfz.ui.base.page.BaseVBFragment


class TabMainFragment : BaseVBFragment<FragmentTabMainBinding>() {

    override fun initView() {
        // 初始化fragment
        val fragmentList = listOf(
            HomeFragment(),
            FuncFragment(),
            MineFragment()
        )
        binding.vpMain.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragmentList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }
        binding.vpMain.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.vpMain.offscreenPageLimit = fragmentList.size
        binding.vpMain.isUserInputEnabled = true

        // 初始化bottombar
        val adapter = BottomBarAdapter(
            listOf(
                TabItemBean(
                    R.drawable.ic_home_active,
                    R.drawable.ic_home_default,
                    R.string.tab_main,
                    R.color.tab_active,
                    R.color.tab_default,
                ),
                TabItemBean(
                    R.drawable.ic_all_active,
                    R.drawable.ic_all_default,
                    R.string.tab_function,
                    R.color.tab_active,
                    R.color.tab_default,
                ),
                TabItemBean(
                    R.drawable.ic_account_active,
                    R.drawable.ic_account_default,
                    R.string.tab_mine,
                    R.color.tab_active,
                    R.color.tab_default,
                )
            )
        )
        binding.rvBottom.adapter = adapter
        binding.rvBottom.layoutManager = GridLayoutManager(context, adapter.length)

        adapter.setOnItemClickListener { _, position ->
            binding.vpMain.setCurrentItem(position, false)
        }

        binding.vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                adapter.setClickIndex(position)
            }
        })

    }
}