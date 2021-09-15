package com.gfz.lab.ui.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gfz.lab.R
import com.gfz.lab.adapter.BottomBarAdapter
import com.gfz.lab.databinding.FragmentTabMainBinding
import com.gfz.lab.model.bean.TabItemBean
import com.gfz.lab.ui.base.BaseVBFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.gfz.lab.base.recyclerview.AvgItemDecoration
import com.gfz.lab.ui.fragment.func.FuncFragment
import com.gfz.lab.ui.fragment.home.HomeFragment
import com.gfz.lab.ui.fragment.mine.MineFragment
import com.gfz.lab.utils.ScreenUtil


class TabMainFragment : BaseVBFragment<FragmentTabMainBinding>() {

    override fun initView() {
        // 初始化fragment
        val fragmentList = listOf(HomeFragment(), FuncFragment(), MineFragment())
        binding.vpMain.adapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int {
                return fragmentList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }
        binding.vpMain.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 初始化bottombar
        val adapter = BottomBarAdapter()
        adapter.setDataList(listOf(TabItemBean(R.drawable.ic_home_active, R.drawable.ic_home_default, R.string.tab_main, R.color.tab_active, R.color.tab_default, R.id.homeFragment),
            TabItemBean(R.drawable.ic_all_active, R.drawable.ic_all_default, R.string.tab_function, R.color.tab_active, R.color.tab_default, R.id.funcFragment),
            TabItemBean(R.drawable.ic_account_active, R.drawable.ic_account_default, R.string.tab_mine, R.color.tab_active, R.color.tab_default, R.id.mineFragment)))
        binding.rvBottom.adapter = adapter
        binding.rvBottom.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvBottom.addItemDecoration(AvgItemDecoration(ScreenUtil.getScreenWidth(), adapter.itemCount))

        adapter.listener = { _, position ->
            binding.vpMain.setCurrentItem(position, false)
        }


        binding.vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                adapter.setClickIndex(position)
            }
        })
    }
}