package com.gfz.common.task

import android.os.Handler

/**
 * 任务顺序执行工具类
 * created by gaofengze on 2021/4/30
 */
class EventListTaskUtil<T>(handler: Handler, runnable: DataRunnable<T>, private val data: List<T>)
    : BaseTaskManager<EventListTaskUtil<T>>(handler) {

    init {
        setRunnable(EventTask(this, runnable))
    }

    private var index: Int = 0

    fun getNextData(): T?{
        return if(canRun()) data[index++] else null
    }

    override fun canRun(): Boolean {
        return index < data.size
    }

    class EventTask<T>(private val taskUtil: EventListTaskUtil<T>, private var runnable: DataRunnable<T>): Runnable{

        override fun run() {
            taskUtil.getNextData()?.let {
                runnable.run(it)
            }
        }
    }

    abstract class DataRunnable<T>: Runnable{
        abstract fun run(data: T)
    }
}