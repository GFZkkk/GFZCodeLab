package com.gfz.mvp.utils

import android.os.SystemClock
import android.util.SparseArray

/**
 * created by gfz on 2020/11/8
 **/
class TimeInterval() {

    private val timeArray : SparseArray<Long> by lazy{
        SparseArray<Long>()
    }

    /**
     * 防快速点击
     */
    fun fastClick(tag: Int = 0, dur: Int) : Boolean{
        if (overTime(tag, dur)){
            start(tag)
            return false
        }
        return true
    }

    fun start(tag: Int = 0){
        timeArray.append(tag, getNow())
    }


    fun overTime(tag: Int = 0, dur: Int): Boolean{
        return getNow() - timeArray.get(tag, 0) > dur
    }

    fun getNow() = SystemClock.uptimeMillis()
}