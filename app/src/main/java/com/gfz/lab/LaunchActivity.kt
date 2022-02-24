package com.gfz.lab

import com.gfz.lab.ui.activity.RootActivity
import com.gfz.ui.base.page.BaseActivity
import me.jessyan.autosize.internal.CancelAdapt

/**
 * 启动页
 * created by gaofengze on 2021/4/30
 */
class LaunchActivity : BaseActivity(), CancelAdapt {

    override fun loadView() {}

    override fun initData() {
        handler.postDelayed({
            if (this.isFinishing) {
                return@postDelayed
            }
            start(RootActivity::class.java)
            finish()
        }, 1000)
    }
}