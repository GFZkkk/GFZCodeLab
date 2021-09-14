package com.gfz.lab

import com.gfz.lab.ui.activity.RootActivity
import com.gfz.lab.ui.base.BaseActivity

/**
 * 启动页
 * created by gaofengze on 2021/4/30
 */
class LaunchActivity : BaseActivity(){

    override fun loadView() {
        // 无ui
    }

    override fun initData() {
        postDelayed({
            if(this.isFinishing){
                return@postDelayed
            }
            start(RootActivity::class.java)
            finish()
        }, 2000)
    }


}