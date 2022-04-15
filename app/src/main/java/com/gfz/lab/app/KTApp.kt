package com.gfz.lab.app

import com.gfz.common.KTCatchException
import com.gfz.common.base.BaseApplication
import com.gfz.common.utils.SpUtil
import com.gfz.common.utils.TopLog
import com.gfz.lab.base.MyFlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.engine.FlutterEngine


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
        FlutterInjector.instance().flutterLoader().startInitialization(this)
        FlutterBoost.instance().setup(this, MyFlutterBoostDelegate()) { engine: FlutterEngine? -> }
    }
}

