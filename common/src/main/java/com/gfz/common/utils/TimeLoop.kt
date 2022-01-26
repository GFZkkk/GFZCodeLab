package com.gfz.common.utils

import android.os.Handler
import android.os.SystemClock

/**
 * 用于建立一个循环事件
 * 在同一场景下有多个Runnable，请提供公共Handler
 * Created by gaofengze on 2021/10/8.
 */
class TimeLoop(private val handler: Handler?, private val period: Int = 1000, runnable: Runnable) {

    private val timeRunnable: Runnable

    var isRun: Boolean
        private set

    private var remove: Boolean

    //初次进入循环的时间点
    private var startTime = -1

    init {
        isRun = false
        remove = false
        timeRunnable = Runnable {
            if (!isRun) {
                return@Runnable
            }
            runnable.run()
            timeLoop()
        }
    }

    /**
     * 在指定延迟后开始循环
     */
    fun runAfterDelay(period: Int = this.period): TimeLoop {
        if (!isRun && !remove && handler != null) {
            isRun = true
            handler.postDelayed(timeRunnable, period.toLong())
        }
        return this
    }

    /**
     * 直接开始循环
     */
    fun run(): TimeLoop {
        if (!isRun && !remove) {
            isRun = true
            timeRunnable.run()
        }
        return this
    }

    fun pause(): TimeLoop {
        isRun = false
        handler?.removeCallbacks(timeRunnable)
        startTime = -1
        return this
    }

    fun remove() {
        remove = true
        pause()
    }

    private fun getStartTime(): Int{
        // 检查补偿时间是否初始化
        if (startTime == -1) {
            startTime = (SystemClock.uptimeMillis() % period).toInt()
        }
        return startTime
    }

    /**
     * 计时器循环
     */
    private fun timeLoop() {
        val now = SystemClock.uptimeMillis()
        val next = now + (period - now % period) + getStartTime()
        handler?.postAtTime(timeRunnable, next)
    }
}