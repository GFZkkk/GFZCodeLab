package com.gfz.common.utils

import android.content.Context
import com.gfz.common.base.BaseApplication
import com.gfz.common.ext.toDP
import com.gfz.common.ext.toPX

/**
 * 屏幕相关操作
 * created by gaofengze on 2021/1/27
 */
object ScreenUtil {
    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(context: Context? = BaseApplication.appContext): Int =
        context?.resources?.displayMetrics?.widthPixels ?: 0


    /**
     * 获取屏幕宽度
     */
    fun getScreenHeight(context: Context? = BaseApplication.appContext): Int =
        context?.resources?.displayMetrics?.heightPixels ?: 0

    fun toPx(dp: Int, context: Context = BaseApplication.appContext): Int = dp.toPX(context)
    fun toDP(px: Int, context: Context = BaseApplication.appContext): Int = px.toDP(context)
}