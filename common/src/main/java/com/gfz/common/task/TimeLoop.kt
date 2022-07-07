package com.gfz.common.task

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.*
import kotlin.concurrent.timer

/**
 * 用于建立一个循环事件
 * 在同一场景下有多个Runnable，请提供公共Handler
 * Created by gaofengze on 2021/10/8.
 */
class TimeLoop (
    private val task: TimeTask,
    private val period: Int = 1000,
    lifecycle: Lifecycle? = null,
    private val runnable: Runnable,
) : Runnable {

    companion object {

        fun createHandlerLoop(
            handler: Handler?,
            period: Int = 1000,
            lifecycle: Lifecycle? = null,
            runnable: Runnable,
        ): TimeLoop {
            val task = HandlerTask(handler)
            return TimeLoop(task, period, lifecycle, runnable)
        }

        fun createTimerLoop(
            period: Int = 1000,
            lifecycle: Lifecycle? = null,
            runnable: Runnable,
        ): TimeLoop {
            val task = TimerTask()
            return TimeLoop(task, period, lifecycle, runnable)
        }
    }

    // 是否正在循环
    var isRun: Boolean
        private set

    // 是否已经移除（不可恢复）
    private var remove: Boolean

    init {
        isRun = false
        remove = false
        task.init(period.toLong(), this)
        lifecycle?.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                remove()
            }
        })
    }

    /**
     * 在指定延迟后开始循环
     */
    fun startAfterDelay(period: Int = this.period): TimeLoop {
        if (!isRun && !remove) {
            isRun = true
            task.startAfterDelay(period.toLong())
        }
        return this
    }

    /**
     * 直接开始循环，不进入message队列
     */
    fun start(): TimeLoop {
        if (!isRun && !remove) {
            isRun = true
            task.start()
        }
        return this
    }

    /**
     * 暂停循环
     */
    fun pause(): TimeLoop {
        isRun = false
        task.pause()
        return this
    }

    /**
     * 移除循环
     */
    fun remove() {
        remove = true
        pause()
    }

    override fun run() {
        if (!isRun) {
            return
        }
        runnable.run()
    }
}

interface TimeTask {

    fun init(period: Long, runnable: Runnable)

    /**
     * 在指定延迟后开始循环
     */
    fun startAfterDelay(delay: Long)

    /**
     * 直接开始循环，不进入message队列
     */
    fun start()

    /**
     * 暂停循环
     */
    fun pause()
}

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

class TimerTask() : TimeTask {

    private var period: Long = 0
    private lateinit var runnable: Runnable

    private var timer: Timer? = null

    override fun init(period: Long, runnable: Runnable) {
        this.period = period
        this.runnable = runnable
    }

    override fun startAfterDelay(delay: Long) {
        timer = timer(initialDelay = delay, period = period) {
            runnable.run()
        }
    }

    override fun start() {
        timer = timer(period = period) {
            runnable.run()
        }
    }

    override fun pause() {
        timer?.cancel()
    }

}