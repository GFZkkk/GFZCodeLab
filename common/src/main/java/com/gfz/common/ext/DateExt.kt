package com.gfz.common.ext

import com.gfz.common.utils.DateUtil
import java.util.*

fun Date.toShortDateStr() = DateUtil.getShortDateStr(this)
fun Date.toShortTimeStr() = DateUtil.getShortTimeStr(this)