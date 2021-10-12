package com.gfz.lab.ext

import com.gfz.common.DateUtil
import java.util.*

fun Date.toShortDateStr() = DateUtil.getShortDateStr(this)
fun Date.toShortTimeStr() = DateUtil.getShortTimeStr(this)