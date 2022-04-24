package com.gfz.lab.ui.fragment

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gfz.lab.R
import com.gfz.lab.adapter.BottomBarAdapter
import com.gfz.lab.databinding.FragmentTabMainBinding
import com.gfz.lab.model.bean.TabItemBean
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.gfz.recyclerview.decoration.AvgItemDecoration
import com.gfz.lab.ui.fragment.func.FuncFragment
import com.gfz.lab.ui.fragment.home.HomeFragment
import com.gfz.lab.ui.fragment.mine.MineFragment
import com.gfz.common.utils.ScreenUtil
import com.gfz.common.utils.TopLog
import com.gfz.ui.base.page.BaseVBFragment
import com.idlefish.flutterboost.containers.FlutterBoostFragment


class TabMainFragment : BaseVBFragment<FragmentTabMainBinding>() {

    override fun initView() {
        // 初始化fragment
        val fragmentList = listOf(HomeFragment(),
            FlutterBoostFragment.CachedEngineFragmentBuilder().url("MessageListPage").build<FlutterBoostFragment>(),
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

        // 初始化bottombar
        val adapter = BottomBarAdapter(listOf(
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
        ))
        binding.rvBottom.adapter = adapter
        binding.rvBottom.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvBottom.addItemDecoration(
            AvgItemDecoration(
                ScreenUtil.getScreenWidth(context),
                adapter.itemCount
            )
        )

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