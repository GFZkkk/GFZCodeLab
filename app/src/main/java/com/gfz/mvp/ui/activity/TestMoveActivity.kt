package com.gfz.mvp.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.mvp.R
import com.gfz.mvp.adapter.TestMoveAdapter
import com.gfz.mvp.base.BaseActivity
import com.gfz.mvp.model.bean.MoveBean
import kotlinx.android.synthetic.main.activity_move.*

/**
 * Created by gaofengze on 2020/8/22.
 */
class TestMoveActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_move
    }

    override fun initView() {

    }

    override fun initData() {
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = TestMoveAdapter(listOf(
            MoveBean("拼写训练"),
            MoveBean("释义训练"),
            MoveBean("例句训练"),
            MoveBean("看词训练"),
            MoveBean("听词训练"),
            MoveBean("译词训练")
        ))
    }
}