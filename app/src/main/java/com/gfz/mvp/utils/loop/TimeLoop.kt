package com.gfz.mvp.utils.loop

import android.os.Handler

/**
 * created by gfz on 2020/11/8
 **/
class TimeLoop(val handler: Handler, val runnable: Runnable, val time: Int) : TaskLoop() {
    init {
        val timeRunnable: Runnable = Runnable {
            if (canLoop()){
                runnable.run()
                loop()
            }
        }
    }

    fun loop(){

    }
}