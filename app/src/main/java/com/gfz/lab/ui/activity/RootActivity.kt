package com.gfz.lab.ui.activity

import androidx.activity.addCallback
import com.gfz.common.ext.toPX
import com.gfz.lab.R
import com.gfz.lab.databinding.ActivityRootBinding
import com.gfz.ui.base.page.BaseVBActivity


/**
 * 主activity
 * created by gaofengze on 2021/5/13
 */
class RootActivity: BaseVBActivity<ActivityRootBinding>(){

    override fun getNavId(): Int = R.id.fcv_host

    override fun initData() {
        20.toPX()
        onBackPressedDispatcher.addCallback {
            if (timeCell.fastClick(0, 1000)){
                finish()
            } else {
                showToast("再按一次退出")
            }
        }
    }
}