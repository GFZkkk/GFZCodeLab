package com.gfz.lab.ext

import com.gfz.lab.utils.DateUtil
import java.util.*

fun Date.toShortDateStr() = DateUtil.getShortDateStr(this)
fun Date.toShortTimeStr() = DateUtil.getShortTimeStr(this)