package com.gfz.mvp.utils

import android.util.Log

object TopLog {
    const val TOPTAG = "com.gfz.mvp"

    fun e(str: String){
        Log.e(TOPTAG, str)
    }

    fun v(str: String){
        Log.v(TOPTAG, str)
    }

    fun i(str: String){
        Log.i(TOPTAG, str)
    }

    fun d(str: String){
        Log.d(TOPTAG, str)
    }
}