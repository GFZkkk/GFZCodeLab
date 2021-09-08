package com.gfz.lab.utils

import android.content.Context
import com.gfz.lab.data.KTApp

/**
 * 屏幕相关操作
 * created by gaofengze on 2021/1/27
 */
object ScreenUtil {
    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(context: Context = KTApp.appContext): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenHeight(context: Context = KTApp.appContext): Int {
        return context.resources.displayMetrics.heightPixels
    }
}