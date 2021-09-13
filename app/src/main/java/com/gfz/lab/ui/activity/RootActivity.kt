package com.gfz.lab.ui.activity

import androidx.navigation.NavController
import com.gfz.lab.R
import com.gfz.lab.databinding.ActivityRootBinding
import com.gfz.lab.ui.base.BaseVBActivity
import com.gfz.lab.utils.TopLog


/**
 * 主activity
 * created by gaofengze on 2021/5/13
 */
class RootActivity: BaseVBActivity<ActivityRootBinding>(){

    override fun getNavId(): Int = R.id.fcv_host

    override fun initData() {
        TopLog.e("RootActivity")
    }


}