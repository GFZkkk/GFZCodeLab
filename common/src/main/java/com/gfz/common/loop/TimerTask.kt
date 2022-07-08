package com.gfz.common.loop

import java.util.*
import kotlin.concurrent.timer

/**
 *
 * created by xueya on 2022/7/8
 */
class TimerTask : TimeTask {

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