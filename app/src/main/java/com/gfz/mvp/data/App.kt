package com.gfz.mvp.data

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

/**
 * created by gaofengze on 2020-01-19
 */

class App: Application() {
    companion object{
        var appContext:Context by Delegates.notNull()
    }
}