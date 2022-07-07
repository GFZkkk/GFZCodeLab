package com.gfz.lab.base

import com.gfz.common.utils.TopLog

/**
 *
 * created by xueya on 2022/4/15
 */
//class MyFlutterBoostDelegate() : FlutterBoostDelegate {
//    override fun pushNativeRoute(options: FlutterBoostRouteOptions?) {
//        //这里根据options.pageName来判断你想跳转哪个页面，这里简单给一个
//        TopLog.e("pushNativeRoute")
//    }
//
//    override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {
//        options?:return
//        val intent = FlutterBoostActivity.CachedEngineIntentBuilder(
//            FlutterBoostActivity::class.java
//        )
//            .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
//            .destroyEngineWithActivity(false)
//            .uniqueId(options.uniqueId())
//            .url(options.pageName())
//            .urlParams(options.arguments())
//            .build(FlutterBoost.instance().currentActivity())
//        FlutterBoost.instance().currentActivity().startActivity(intent)
//    }
//}