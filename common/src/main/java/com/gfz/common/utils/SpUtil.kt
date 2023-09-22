package com.gfz.common.utils

import android.content.Context
import com.tencent.mmkv.MMKV

object SpUtil {

    fun init(appContext: Context) {
        MMKV.initialize(appContext)
    }

    private val sp: MMKV by lazy {
        MMKV.defaultMMKV(MMKV.MULTI_PROCESS_MODE, null)
    }

    fun putString(key: String, value: String) {
        sp.encode(key, value)
    }

    fun <T> get(key: String, default: T? = null): T? {
        return when (default) {
            is String -> sp.decodeString(key, default)
            is Boolean -> sp.decodeBool(key, default)
            is Int -> sp.decodeInt(key, default)
            is Float -> sp.decodeFloat(key, default)
            is Double -> sp.decodeDouble(key, default)
            is ByteArray -> sp.decodeBytes(key, default)
            else -> throw IllegalArgumentException("MMKV get Unknown Type")
        } as T
    }
}