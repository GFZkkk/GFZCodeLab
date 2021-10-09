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

    fun getUserKey(key: String?): String? = key?.let {
        if (it.startsWith("U_")){
            val userId = "userId"
            it.replaceFirst("U", userId)
        } else {
            key
        }
    }
}