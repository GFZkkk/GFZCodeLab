package com.gfz.mvp.ui.activity

import com.gfz.mvp.R
import com.gfz.mvp.base.BaseActivity

/**
 * Created by gaofengze on 2020/9/15.
 */
class TestRecordActivity : BaseActivity()  {
    override fun getLayoutId(): Int {
        return R.layout.activity_record
    }

    override fun initView() {

    }

    override fun initData() {
//        ApiFactory.instance.queryLogByUserId()
    }
}