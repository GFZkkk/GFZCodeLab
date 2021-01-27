package com.gfz.mvp.utils

import android.os.SystemClock
import android.util.SparseArray

class TimeCell(size: Int = 10) {

    private val timeArray: SparseArray<Long> by lazy {
        SparseArray<Long>(size)
    }

    private var lastTime = 0L

    fun fastClick(tag: Int, dur: Int): Boolean {
        val time = SystemClock.elapsedRealtime()
        val last = if(tag == 0){lastTime}else{timeArray.get(tag, 0L)}
        if (time - last < dur) {
            if (time == last){
                TopLog.e("点击间隔为0，请检查是否存在重复防重点击")
            }
            return true
        }
        if (tag == 0){
            lastTime = time
        }else{
            timeArray.append(tag, time)
        }
        return false
    }
}