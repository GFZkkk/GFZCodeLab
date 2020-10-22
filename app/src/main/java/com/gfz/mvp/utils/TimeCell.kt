package com.gfz.mvp.utils

import android.os.SystemClock
import android.util.SparseArray

class TimeCell(size: Int = 10) {

    private val timeArray: SparseArray<Long> by lazy {
        SparseArray<Long>(size)
    }

    fun fastClick(tag: Int, dur: Int): Boolean {
        val time = SystemClock.elapsedRealtime()
        val timeCell: Long = time - timeArray.get(tag, 0L)
        if (timeCell < dur) {
            return true
        }
        timeArray.append(tag, time)
        return false
    }
}