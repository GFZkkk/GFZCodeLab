package com.gfz.mvp.activity

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gfz.mvp.R
import com.gfz.mvp.adapter.BottomBarAdapter
import com.gfz.mvp.base.BaseActivity
import com.gfz.mvp.base.recyclerview.NormalItemDecoration
import com.gfz.mvp.databinding.ActicityTabMainBinding

import com.gfz.mvp.model.bean.TabItemBean
import com.gfz.mvp.utils.ScreenUtil
import com.gfz.mvp.utils.getScreenWidth
import com.gfz.mvp.utils.viewBind
import java.util.*

/**
 * 主activity
 * created by gaofengze on 2021/5/13
 */
class TabMainActivity: BaseActivity() {

    private val binding: ActicityTabMainBinding by viewBind()
    private val tabBarAdapter: BottomBarAdapter = BottomBarAdapter()
    private var pageIndex = 0

    override fun initData() {
        val screenWidth = getScreenWidth()
        with(binding){
            val bottomBarList = listOf(
                TabItemBean(R.drawable.ic_home_active, R.drawable.ic_home_default, "首页", R.color.tab_active, R.color.tab_default),
                TabItemBean(R.drawable.ic_all_active, R.drawable.ic_all_default, "功能", R.color.tab_active, R.color.tab_default),
                TabItemBean(R.drawable.ic_account_active, R.drawable.ic_account_default, "我的", R.color.tab_active, R.color.tab_default),
            )
            tabBarAdapter.setDataList(bottomBarList)
            tabBarAdapter.setClickIndex(pageIndex)
            rvBottom.layoutManager = LinearLayoutManager(this@TabMainActivity, RecyclerView.HORIZONTAL, false)
            rvBottom.addItemDecoration(NormalItemDecoration(screenWidth, bottomBarList.size))
            rvBottom.adapter = tabBarAdapter
            tabBarAdapter.setOnItemClickListener { _, pos ->
                pageIndex = pos
            }
        }
    }
}