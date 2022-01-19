package com.gfz.common.utils

import android.os.SystemClock
import android.util.SparseArray

/**
 * 时间间隔工具类
 */
class TimeCell(size: Int = 5) {

    private val timeArray: SparseArray<Long> by lazy {
        SparseArray<Long>(size)
    }

    private var lastTime = 0L

    /**
     * 是否是重复点击
     */
    fun fastClick(tag: Int = 0, dur: Int): Boolean {
        val now = getNowTime()
        val last = getLastTime(tag)
        if (!overTimeInterval(now, last, dur)) {
            return true
        }
        saveLastTime(tag, now)
        return false
    }

    /**
     * 开始计时
     */
    fun start(tag: Int = 0) {
        saveLastTime(tag, getNowTime())
    }

    /**
     * 结束计时
     */
    fun end(tag: Int = 0): Long {
        val time = getNowTime() - getLastTime(tag)
        start(tag)
        return time
    }

    /**
     * 是否超时
     */
    fun overTime(dur: Int, tag: Int = 0): Boolean {
        return overTimeInterval(getNowTime(), getLastTime(tag), dur)
    }

    fun isNewTag(tag: Int): Boolean {
        return if (tag == 0) {
            lastTime == 0L
        } else {
            timeArray.indexOfKey(tag) < 0
        }
    }

    /**
     * 判断两个时间的间隔是否已经超过条件
     */
    private fun overTimeInterval(now: Long, last: Long, dur: Int): Boolean {
        return now - last > dur
    }

    /**
     * 获取当前时间
     */
    private fun getNowTime(): Long {
        return SystemClock.elapsedRealtime()
    }

    /**
     * 获取上一次记录时间
     */
    private fun getLastTime(tag: Int): Long {
        return if (tag == 0) {
            lastTime
        } else {
            timeArray[tag, 0L]
        }
    }

    /**
     * 保存记录时间
     */
    private fun saveLastTime(tag: Int, now: Long) {
        if (tag == 0) {
            lastTime = now
        } else {
            timeArray.append(tag, now)
        }
    }
}