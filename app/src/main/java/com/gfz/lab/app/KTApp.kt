package com.gfz.lab.app

import android.app.Application
import android.content.Context
import com.gfz.common.utils.SpUtil
import com.gfz.common.KTCatchException
import com.gfz.common.base.BaseApplication
import com.gfz.common.utils.TopLog

/**
 * created by gaofengze on 2020-01-19
 */

class KTApp : BaseApplication() {

    companion object {
        const val test = false
    }

    override fun init() {
        TopLog.init(true)
        KTCatchException.init(this)
        SpUtil.init(appContext)
    }
}