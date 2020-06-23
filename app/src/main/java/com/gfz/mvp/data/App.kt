package com.gfz.mvp.data

import android.app.Application
import android.content.Context
import com.gfz.mvp.utils.TopLog
import kotlin.properties.Delegates

/**
 * created by gaofengze on 2020-01-19
 */

class App: Application() {
    companion object{
        var appContext: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        TopLog.init(true)
    }
}