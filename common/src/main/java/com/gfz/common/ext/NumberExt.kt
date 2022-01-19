package com.gfz.common.ext

import android.content.Context
import com.gfz.common.base.BaseApplication

/**
 *
 * created by gaofengze on 2021/10/8
 */

/**
 * 根据手机的分辨率从 dx(像素) 的单位 转成为 px
 */
fun Int.toPX(context: Context = BaseApplication.appContext): Int =
    (this * context.resources.displayMetrics.density + 0.5f).toInt()

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun Int.toDP(context: Context = BaseApplication.appContext): Int =
    (this / context.resources.displayMetrics.density + 0.5f).toInt()

fun Int.round(min: Int, max: Int): Int {
    return when {
        this > max -> {
            max
        }
        this < min -> {
            min
        }
        else -> {
            this
        }
    }
}

fun Float.round(min: Float, max: Float): Float {
    return when {
        this > max -> {
            max
        }
        this < min -> {
            min
        }
        else -> {
            this
        }
    }
}