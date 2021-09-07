package com.gfz.mvp.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gfz.mvp.R
import com.gfz.mvp.adapter.BottomBarAdapter
import com.gfz.mvp.base.BaseFragment
import com.gfz.mvp.base.recyclerview.NormalItemDecoration
import com.gfz.mvp.databinding.FragmentTabMainBinding
import com.gfz.mvp.model.bean.TabItemBean
import com.gfz.mvp.utils.ScreenUtil.getScreenWidth
import com.gfz.mvp.utils.viewBind

/**
 * tab
 * created by gaofengze on 2021/5/14
 */
class TabMainFragment : BaseFragment(R.layout.fragment_tab_main) {
    private val binding: FragmentTabMainBinding by viewBind()

    override fun initView() {
        val screenWidth = getScreenWidth()
        val tabBarAdapter = BottomBarAdapter()
        var pageIndex = 0
        val fragmentList = listOf(HomeFragment(), FuncFragment(), MineFragment())
        handlerFragment{ transition ->
            fragmentList.forEach {
                transition.add(getFragmentLayoutId(), it)
            }
        }
        with(binding){
            val bottomBarList = listOf(
                TabItemBean(R.drawable.ic_home_active, R.drawable.ic_home_default, "首页", R.color.tab_active, R.color.tab_default),
                TabItemBean(R.drawable.ic_all_active, R.drawable.ic_all_default, "功能", R.color.tab_active, R.color.tab_default),
                TabItemBean(R.drawable.ic_account_active, R.drawable.ic_account_default, "我的", R.color.tab_active, R.color.tab_default),
            )
            tabBarAdapter.setDataList(bottomBarList)
            tabBarAdapter.setClickIndex(pageIndex)
            rvBottom.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rvBottom.addItemDecoration(NormalItemDecoration(screenWidth, bottomBarList.size))
            rvBottom.adapter = tabBarAdapter
            tabBarAdapter.setOnItemClickListener { _, pos ->
                if (pageIndex == pos){
                    return@setOnItemClickListener
                }
                pageIndex = pos
            }
        }
    }

    fun getFragmentLayoutId() = R.id.fl_body

}