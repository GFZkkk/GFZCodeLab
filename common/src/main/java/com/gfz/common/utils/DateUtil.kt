package com.gfz.common.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    /**
     * 所需时间格式
     */
    private const val shortDateFormatStr = "yyyy-MM-dd"

    /**
     * 所需时间格式
     */
    private const val shortTimeFormatStr = "yyyy-MM-dd HH:mm:ss"

    /**
     * 获取当前年月日
     */
    fun getShortDateStr(date: Date = Date()): String {
        return SimpleDateFormat(shortDateFormatStr, Locale.getDefault()).format(date)
    }

    /**
     * 获取当前年月日
     */
    fun getShortTimeStr(date: Date = Date()): String {
        return SimpleDateFormat(shortTimeFormatStr, Locale.getDefault()).format(date)
    }

    fun addDay(day: Int, date: Date = Date()): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, day)
        return calendar.time
    }

    fun addMonth(month: Int, date: Date = Date()): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, month)
        return calendar.time
    }

    fun addYear(year: Int, date: Date = Date()): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.YEAR, year)
        return calendar.time
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