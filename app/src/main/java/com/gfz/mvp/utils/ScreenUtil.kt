package com.gfz.mvp.utils

import android.content.Context

/**
 * 屏幕相关操作
 * created by gaofengze on 2021/1/27
 */
object ScreenUtil {
    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }
}