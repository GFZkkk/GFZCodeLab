package com.gfz.mvp.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    /**
     * 所需时间格式
     */
    private const val shortDateFormatStr = "yyyy-MM-dd"

    /**
     * 获取当前年月日
     */
    fun getCurStandardShortDate(): String {
        return SimpleDateFormat(shortDateFormatStr, Locale.getDefault()).format(Date())
    }

    /**
     * 获取某个月的天数
     */
    fun getMonthDay(year: Int, month: Int): Int {
        return when (month) {
            4, 6, 9, 11 -> 30
            2 -> if (year % 400 == 0 || year % 4 == 0 && year % 100 != 0) 29 else 28
            else -> 31
        }
    }

}