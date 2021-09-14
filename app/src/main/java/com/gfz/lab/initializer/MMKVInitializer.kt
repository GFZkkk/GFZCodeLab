package com.gfz.lab.initializer
import android.content.Context
import androidx.startup.Initializer
import com.gfz.lab.utils.TopLog
import com.tencent.mmkv.MMKV

class MMKVInitializer: Initializer<String> {

    override fun create(context: Context): String {
        TopLog.e("MMKVInitializer")
        return MMKV.initialize(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

}