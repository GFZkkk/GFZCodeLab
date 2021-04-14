package com.gfz.mvp.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.mvp.adapter.TestExtAdapter
import com.gfz.mvp.adapter.TestListAdapter
import com.gfz.mvp.base.BaseActivity
import com.gfz.mvp.callback.OnItemClickListener
import com.gfz.mvp.databinding.ActivityExtlayoutBinding
import com.gfz.mvp.databinding.LayoutHeaderTestBinding
import com.gfz.mvp.utils.toLog
import com.gfz.mvp.utils.viewBind
import java.util.*
import kotlin.collections.ArrayList

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