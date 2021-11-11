package com.gfz.common.utils

import android.content.Context

/**
 * 屏幕相关操作
 * created by gaofengze on 2021/1/27
 */
object ScreenUtil {
    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(context: Context?): Int = context?.resources?.displayMetrics?.widthPixels ?:0


    /**
     * 获取屏幕宽度
     */
    fun getScreenHeight(context: Context?): Int = context?.resources?.displayMetrics?.heightPixels ?:0
}