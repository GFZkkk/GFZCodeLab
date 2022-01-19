package com.gfz.common.ext

import com.gfz.common.utils.TopLog


fun Any.toLog(type: Int = TopLog.E, tag: String? = null) {
    TopLog.printLog(type, tag, this)
}