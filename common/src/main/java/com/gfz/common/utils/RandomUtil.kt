package com.gfz.common.utils

import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

/**
 * 随机数工具类
 * created by gaofengze on 2021/4/30
 */
object RandomUtil {

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

    fun getRandomIndex(size: Int): Int {
        return ThreadLocalRandom.current().nextInt(size)
    }

    /**
     * 乱序一个list
     */
    fun <T> randomList(list: MutableList<T>, num: Int = list.size) {
        if (num <= 0) {
            return
        }
        val size = num.coerceAtMost(list.size)
        list.forEachIndexed { index, t ->
            val randomIndex = getRandomIndex(size)
            list[index] = list[randomIndex]
            list[randomIndex] = t
        }
    }
}