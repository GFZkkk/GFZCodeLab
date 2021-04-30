package com.gfz.mvp.module

import android.os.Handler
import android.os.Looper


/**
 * 基础的任务执行类
 * created by gaofengze on 2021/4/29
 */
open class BaseTaskManager<T : BaseTaskManager<T>>(
    private val handler: Handler = Handler(Looper.getMainLooper()),
    private var runnable: Runnable? = null) {

    private var start = false
    private var remove = false
    private var callback: FinishCallback? = null
    private val task: Runnable
        get() = checkNotNull(runnable)

    interface FinishCallback{
        fun finish()
    }

    fun setCallback(callback: FinishCallback?): T{
        this.callback = callback
        return this as T
    }

    fun setRunnable(runnable: Runnable){
        this.runnable = runnable
    }

    fun run(): T{
        return runAfterDelay(0)
    }

    open fun runAfterDelay(delay: Long): T{
        if (!start && !remove){
            handler.postDelayed(task, delay)
            start = true
        }
        return this as T
    }

    fun next(delay: Long = 300){
        if (start && !remove){
            if (canRun()){
                handler.postDelayed(task, delay)
            }else{
                remove()
                callback?.finish()
            }
        }
    }

    fun pause(){
        handler.removeCallbacks(task)
        start = false
    }

    fun remove(){
        pause()
        remove = false
    }

    open fun canRun(): Boolean{
        return true
    }

}