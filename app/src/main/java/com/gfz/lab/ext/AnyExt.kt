package com.gfz.lab.ext

import com.gfz.common.TopLog


fun Any.toLog(type: Int = TopLog.E, tag: String? = null){
    TopLog.printLog(type, tag, this)
}