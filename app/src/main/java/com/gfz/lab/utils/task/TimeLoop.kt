package com.gfz.lab.utils.task

import android.os.Handler
import android.os.SystemClock

/**
 * 用于建立一个循环事件
 * 在同一场景下有多个Runnable，请提供公共Handler
 * Created by gaofengze on 2021/10/8.
 */
class TimeLoop(private val handler: Handler?, runnable: Runnable, private val time: Int) {

    private val timeRunnable: Runnable

    var isRun: Boolean
        private set

    private var remove: Boolean

    //补偿时间
    private var makeUpTime = -1

    init {
        isRun = false
        remove = false
        timeRunnable = Runnable {
            if (isRun) {
                if (makeUpTime == -1) {
                    makeUpTime = (SystemClock.uptimeMillis() % time).toInt()
                }
                runnable.run()
                timeLoop()
            }
        }
    }

    /**
     * 在指定延迟后开始循环
     */
    fun runAfterDelay(time: Int = this.time): TimeLoop {
        if (!isRun && !remove && handler != null) {
            isRun = true
            handler.postDelayed(timeRunnable, time.toLong())
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
        makeUpTime = -1
        return this
    }

    fun remove() {
        remove = true
        pause()
    }

    /**
     * 计时器循环
     */
    private fun timeLoop() {
        val now = SystemClock.uptimeMillis()
        val next = now + (time - now % time) + makeUpTime
        handler?.postAtTime(timeRunnable, next)
    }
}