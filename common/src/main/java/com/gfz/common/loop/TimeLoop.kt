package com.gfz.common.loop

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import java.util.*
import kotlin.concurrent.timer

/**
 * 用于建立一个循环事件
 * 在同一场景下有多个Runnable，请提供公共Handler
 * Created by gaofengze on 2021/10/8.
 */
class TimeLoop(
    private val task: TimeTask,
    private val period: Int = 1000,
    private val runOnUIThread: Boolean = true,
    lifecycle: Lifecycle? = null,
    private val runnable: Runnable,
) : Runnable {

    companion object {

        fun createHandlerLoop(
            handler: Handler?,
            period: Int = 1000,
            lifecycle: Lifecycle? = null,
            runOnUIThread: Boolean = true,
            runnable: Runnable,
        ): TimeLoop {
            val task = HandlerTask(handler)
            return TimeLoop(task, period, runOnUIThread, lifecycle, runnable)
        }

        fun createTimerLoop(
            period: Int = 1000,
            lifecycle: Lifecycle? = null,
            runOnUIThread: Boolean = true,
            runnable: Runnable,
        ): TimeLoop {
            val task = TimerTask()
            return TimeLoop(task, period, runOnUIThread, lifecycle, runnable)
        }
    }

    // 是否正在循环
    var isRun: Boolean
        private set

    // 是否已经移除（不可恢复）
    private var remove: Boolean

    private val mainHandler by lazy {
        Handler(Looper.getMainLooper())
    }

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
        if (runOnUIThread && !isUIThread()) {
            mainHandler.post {
                runnable.run()
            }
        } else {
            runnable.run()
        }
    }

    private fun isUIThread(): Boolean = Looper.getMainLooper().thread == Thread.currentThread()

}


