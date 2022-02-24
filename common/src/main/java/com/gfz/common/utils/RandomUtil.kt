package com.gfz.common.utils

import android.os.SystemClock
import java.util.*
import kotlin.collections.ArrayList

/**
 * 随机数工具类
 * created by gaofengze on 2021/4/30
 */
object RandomUtil {

    private val random: Random by lazy {
        Random()
    }

    fun getRandomIndex(size: Int): Int {
        random.setSeed(SystemClock.uptimeMillis())
        return random.nextInt(size)
    }

    /**
     * 从list中取一个随机的list,不改变原有list
     */
    fun <T> getRandomList(list: List<T>, num: Int = list.size): List<T> {
        if (num <= 0) {
            return ArrayList()
        }
        val newList = ArrayList(list)
        randomList(newList, num)
        return if (num >= newList.size) {
            newList
        } else {
            newList.subList(0, num)
        }
    }

    /**
     * 乱序一个list
     */
    fun <T> randomList(list: MutableList<T>, num: Int = list.size) {
        if (num <= 0) {
            return
        }
        random.setSeed(SystemClock.uptimeMillis())
        val size = num.coerceAtMost(list.size)
        list.forEachIndexed { index, t ->
            val randomIndex = random.nextInt(size)
            list[index] = list[randomIndex]
            list[randomIndex] = t
        }
    }
}