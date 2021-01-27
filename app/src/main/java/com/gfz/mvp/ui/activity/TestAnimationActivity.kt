package com.gfz.mvp.ui.activity

import com.gfz.mvp.R
import com.gfz.mvp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_record.*

/**
 * Created by gaofengze on 2020/9/15.
 */
class TestAnimationActivity : BaseActivity()  {

    override fun getLayoutId(): Int {
        return R.layout.activity_record
    }

    override fun initView() {

    }

    override fun initData() {
        fw.start()
        fw.setOnClickListener {
            fw.start()
        }
    }
}