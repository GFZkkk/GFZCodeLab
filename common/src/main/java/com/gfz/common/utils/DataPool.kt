package com.gfz.common.utils

import android.util.SparseArray

/**
 *
 * created by xueya on 2022/3/28
 */
class DataPool<T>(initialCapacity: Int = 10, private val defaultAll: () -> T) {
    private val pool = SparseArray<T>(initialCapacity)
    var showLog = false
    private var total = 0
    private var success = 0

    operator fun get(key: Int, default: () -> T = defaultAll): T {
        val data = pool[key]
        total++
        return if (data is T) {
            success++
            log()
            data
        } else {
            log()
            default.invoke().apply {
                pool.append(key, this)
            }
        }
    }

    operator fun set(key: Int, value: T){
        pool.append(key, value)
    }


    fun log() {
        if (!showLog) return
        TopLog.e("success:$success | total:$total | ${1f * success / total}")
    }
}