package com.gfz.recyclerview.bean

/**
 * created by gfz on 2020/4/6
 **/
open class BaseCalendarBean(val date: IntArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseCalendarBean

        return date.contentEquals(other.date)
    }

    override fun hashCode(): Int {
        return date.contentHashCode()
    }
}