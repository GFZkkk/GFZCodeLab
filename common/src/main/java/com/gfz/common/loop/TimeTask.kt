package com.gfz.common.loop

/**
 *
 * created by xueya on 2022/7/8
 */
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