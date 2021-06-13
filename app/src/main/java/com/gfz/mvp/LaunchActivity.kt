package com.gfz.mvp

import android.content.Intent
import com.gfz.mvp.activity.MainActivity
import com.gfz.mvp.activity.TabMainActivity
import com.gfz.mvp.base.BaseActivity

/**
 * 启动页
 * created by gaofengze on 2021/4/30
 */
class LaunchActivity : BaseActivity(){

    override fun initData() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}