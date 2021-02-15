package com.gfz.mvp.utils.loop

import android.os.Handler

/**
 * created by gfz on 2020/11/8
 **/
open class TaskLoop {

    private var isRun : Boolean = false
    private var isEnd : Boolean = false
    val taskRunnable: Runnable? = null
    val handler: Handler? = null

    fun canLoop() = !isEnd && isRun

    /**
     *  开始任务
     */
    fun startTask(){
        if (!isEnd){
            isRun = true
            taskRunnable?.run()
        }

    }

    /**
     * 延迟一段时间后开始任务
     */
    fun startAfterDelay(delay: Long){
        if (!isEnd){
            isRun = true
            taskRunnable?.apply {
                handler?.postDelayed(this, delay)
            }
        }
    }

    /**
     * 暂停任务
     */
    fun pauseTask(){
        isRun = false
        taskRunnable?.apply {
            handler?.removeCallbacks(this)
        }

    }

    /**
     *  结束任务
     */
    fun endTask(){
        isEnd = true
        pauseTask()
    }





}