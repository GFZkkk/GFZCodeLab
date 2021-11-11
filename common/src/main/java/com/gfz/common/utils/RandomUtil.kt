package com.gfz.common.utils

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

    fun getRandomIndex(size: Int): Int{
        random.setSeed(System.currentTimeMillis())
        return random.nextInt(size)
    }

    /**
     * 从list中取一个随机的list,不改变原有list
     */
    fun <T> getRandomList(list: MutableList<T>, num: Int = list.size): List<T>{

        randomList(ArrayList(list), num)

        return if (num >= list.size) { list } else { list.subList(0, num) }
    }

    /**
     * 乱序一个list
     */
    fun <T> randomList(list: MutableList<T>, num: Int = list.size){

        random.setSeed(System.currentTimeMillis())

        val size = num.coerceAtMost(list.size)

        list.forEachIndexed { index, t ->
            val randomIndex = random.nextInt(size)
            list[index] = list[randomIndex]
            list[randomIndex] = t
        }
    }
}