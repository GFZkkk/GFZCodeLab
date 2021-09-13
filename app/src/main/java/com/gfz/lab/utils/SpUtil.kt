package com.gfz.lab.utils

import com.tencent.mmkv.MMKV

object SpUtil {
    private val sp: MMKV by lazy {
        MMKV.defaultMMKV()
    }

    fun putString(key: String, value: String){
        sp.encode(getUserKey(key), value)
    }

    fun getString(key: String?, default: String? = null): String? {
        return sp.getString(getUserKey(key), default)
    }

    fun getUserKey(key: String?): String?{
        return key?.let {
            if (key.startsWith("U_")){
                val userId = "userId"
                return key.replaceFirst("U", "userId")
            }
            return key
        }
    }
}