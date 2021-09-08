package com.gfz.lab.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.lab.adapter.TestExtAdapter
import com.gfz.lab.base.BaseActivity
import com.gfz.lab.databinding.ActivityExtlayoutBinding
import com.gfz.lab.databinding.LayoutHeaderTestBinding
import com.gfz.lab.utils.viewBind
import java.util.*

/**
 *
 * created by gaofengze on 2021/2/22
 */
class TestExtAdapterActivity : BaseActivity() {

    private val binding: ActivityExtlayoutBinding by viewBind()

    override fun initData() {

        with(binding) {

            val adapter = TestExtAdapter()
            rvList.adapter = adapter
            rvList.layoutManager = LinearLayoutManager(this@TestExtAdapterActivity)
            adapter.refresh(listOf(1,2,3,4))
            val headerBinding: LayoutHeaderTestBinding = viewBind(rvList)

            adapter.headerViewBinding = headerBinding
            headerBinding.tvHeader.text = "测试"

        }
    }
}