package com.gfz.lab.ui.activity

import android.view.View
import android.view.WindowManager
import com.gfz.common.ext.getCompatColor
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
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = getCompatColor(R.color.white)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun onBackPressed() {
        if (onBackPressedDispatcher.hasEnabledCallbacks()){
            super.onBackPressed()
        } else {
            if (timeCell.fastClick(0, 1000)){
                finish()
            } else {
                showToast("再按一次退出")
            }
        }
    }
}