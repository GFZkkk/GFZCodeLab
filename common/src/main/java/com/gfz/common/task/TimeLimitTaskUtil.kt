package com.gfz.common.task

import android.os.Handler
import com.gfz.common.utils.TimeCell


/**
 * 时间限制的轮询任务，需要手动调用next方法
 * created by gaofengze on 2021/4/30
 */
class TimeLimitTaskUtil( private val timeLimit: Int, handler: Handler, runnable: Runnable) :
    BaseTaskManager<TimeLimitTaskUtil>(handler, runnable) {

    private var timeCell: TimeCell = TimeCell()

    override fun runAfterDelay(delay: Long): TimeLimitTaskUtil {
        timeCell.start()
        return super.runAfterDelay(delay)
    }

    override fun canRun(): Boolean {
        return timeCell.overTime(timeLimit)
    }

}