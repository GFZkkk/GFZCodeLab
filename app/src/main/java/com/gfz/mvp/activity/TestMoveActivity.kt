package com.gfz.mvp.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.mvp.adapter.TestMoveAdapter
import com.gfz.mvp.base.BaseActivity
import com.gfz.mvp.databinding.ActivityMoveBinding
import com.gfz.mvp.model.bean.MoveBean
import com.gfz.mvp.utils.viewBind

/**
 * Created by gaofengze on 2020/8/22.
 */
class TestMoveActivity : BaseActivity() {

    private val binding: ActivityMoveBinding by viewBind()

    override fun initData() {
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = TestMoveAdapter(listOf(
            MoveBean("拼写训练"),
            MoveBean("释义训练"),
            MoveBean("例句训练"),
            MoveBean("看词训练"),
            MoveBean("听词训练"),
            MoveBean("译词训练")
        ))
    }
}