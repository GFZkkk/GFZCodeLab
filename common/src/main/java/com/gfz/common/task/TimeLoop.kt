package com.gfz.common.task

import android.os.Handler
import android.os.SystemClock

/**
 * 用于建立一个循环事件
 * 在同一场景下有多个Runnable，请提供公共Handler
 * Created by gaofengze on 2021/10/8.
 */
class TimeLoop(private val handler: Handler?, private val period: Int = 1000, runnable: Runnable) {

    // 自循环的任务
    private val timeRunnable: Runnable

    // 是否正在循环
    var isRun: Boolean
        private set

    // 是否已经移除（不可恢复）
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
     * 直接开始循环，不进入message队列
     */
    fun run(): TimeLoop {
        if (!isRun && !remove) {
            isRun = true
            timeRunnable.run()
        }
        return this
    }

    /**
     * 暂停循环
     */
    fun pause(): TimeLoop {
        isRun = false
        handler?.removeCallbacks(timeRunnable)
        startTime = -1
        return this
    }

    /**
     * 移除循环
     */
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