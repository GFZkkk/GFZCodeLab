package com.gfz.common.loop

import android.os.Handler
import android.os.SystemClock

/**
 *
 * created by xueya on 2022/7/8
 */

class HandlerTask(
    private val handler: Handler?,
) : TimeTask {
    private var period: Long = 0L

    // 自循环的任务
    private lateinit var timeRunnable: Runnable

    //初次进入循环的时间点
    private var startTime = -1

    override fun init(period: Long, runnable: Runnable) {
        this.period = period
        timeRunnable = Runnable {
            runnable.run()
            timeLoop()
        }
    }

    override fun startAfterDelay(delay: Long) {
        handler?.postDelayed(timeRunnable, delay)
    }

    override fun start() {
        timeRunnable.run()
    }

    override fun pause() {
        handler?.removeCallbacks(timeRunnable)
        startTime = -1
    }

    private fun getStartTime(): Int {
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