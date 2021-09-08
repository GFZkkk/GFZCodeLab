package com.gfz.lab

import android.content.Intent
import com.gfz.lab.activity.RootActivity
import com.gfz.lab.base.BaseActivity

/**
 * 启动页
 * created by gaofengze on 2021/4/30
 */
class LaunchActivity : BaseActivity(){

    override fun initData() {
        startActivity(Intent(this, RootActivity::class.java))
        finish()
    }
}