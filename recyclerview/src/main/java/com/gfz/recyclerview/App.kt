package com.gfz.recyclerview

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

class App : Application() {
    companion object{
        var appContext: Context by Delegates.notNull()
    }
}