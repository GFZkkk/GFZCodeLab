package com.gfz.lab.utils.task

import android.os.Handler
import com.gfz.lab.utils.TimeCell


/**
 * 时间限制的轮询任务，需要手动调用next方法
 * created by gaofengze on 2021/4/30
 */
class TimeLimitTaskUtil(handler: Handler, runnable: Runnable, private val timeLimit: Int)
    : BaseTaskManager<TimeLimitTaskUtil>(handler, runnable){

    private var timeCell: TimeCell = TimeCell()

    override fun runAfterDelay(delay: Long): TimeLimitTaskUtil {
        timeCell.start()
        return super.runAfterDelay(delay)
    }

    override fun canRun(): Boolean {
        return timeCell.overTime(timeLimit)
    }

}