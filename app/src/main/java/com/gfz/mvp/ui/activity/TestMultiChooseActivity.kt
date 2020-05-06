package com.gfz.mvp.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.mvp.R
import com.gfz.mvp.adapter.TestMutilChooseAdapter
import com.gfz.mvp.base.mvp.BaseActivity
import com.gfz.mvp.model.bean.MultipleChooseBean
import kotlinx.android.synthetic.main.activity_multi_choose.*
import java.util.*


/**
 * created by gaofengze on 2020/4/14
 */

class TestMultiChooseActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_multi_choose

    override fun initView() {

    }

    override fun initData() {
        val rvList = rv_list
        rvList.layoutManager = LinearLayoutManager(this)
        val adapter = TestMutilChooseAdapter()

        adapter.addAll(object : ArrayList<MultipleChooseBean>() {
            init {
                add(MultipleChooseBean("这是第一条", 1))
                add(MultipleChooseBean("这是第二条", 1))
                add(MultipleChooseBean("这是第三条", 2))
                add(MultipleChooseBean("这是第四条", 3))
                add(MultipleChooseBean("这是第五条", 4))
                add(MultipleChooseBean("这是第六条", 4))
                add(MultipleChooseBean("这是第七条", 4))
                add(MultipleChooseBean("这是第八条", 5))
                add(MultipleChooseBean("这是第九条", 5))
                add(MultipleChooseBean("这是第十条", 5))
                add(MultipleChooseBean("这是第十一条", 5))
                add(MultipleChooseBean("这是第十二条", 3))
                add(MultipleChooseBean("这是第一条", 1))
                add(MultipleChooseBean("这是第二条", 1))
                add(MultipleChooseBean("这是第三条", 2))
                add(MultipleChooseBean("这是第四条", 3))
                add(MultipleChooseBean("这是第五条", 4))
                add(MultipleChooseBean("这是第六条", 4))
                add(MultipleChooseBean("这是第七条", 4))
                add(MultipleChooseBean("这是第八条", 5))
                add(MultipleChooseBean("这是第九条", 5))
                add(MultipleChooseBean("这是第十条", 5))
                add(MultipleChooseBean("这是第十一条", 5))
                add(MultipleChooseBean("这是第十二条", 3))
            }
        })

        rvList.adapter = adapter
    }
}