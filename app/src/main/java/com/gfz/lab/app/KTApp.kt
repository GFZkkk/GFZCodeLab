package com.gfz.lab.app

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import androidx.multidex.MultiDex
import com.gfz.common.utils.SpUtil
import com.gfz.common.KTCatchException
import com.gfz.common.utils.TopLog
import kotlin.properties.Delegates

/**
 * created by gaofengze on 2020-01-19
 */

class KTApp: Application() {

    companion object{
        var appContext: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        if (shouldInit()){
            init()
        }

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun init(){
        TopLog.init(true)
        KTCatchException.init(this)

        SpUtil.init(appContext)
    }

    private fun shouldInit(): Boolean {
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val processInfos = am.runningAppProcesses
        val mainProcessName = packageName
        val myPid = Process.myPid()
        for (info in processInfos) {
            if (info.pid == myPid && mainProcessName == info.processName) {
                return true
            }
        }
        return false
    }
}