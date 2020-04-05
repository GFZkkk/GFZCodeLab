package com.gfz.mvp.utils

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.gfz.mvp.data.App

/**
 * 根据资源id获取颜色
 */
fun getColor(resId: Int) = ContextCompat.getColor(App.appContext, resId)

/**
 * 根据资源id获取图片
 */
fun getDrawable(resId: Int): Drawable? = ContextCompat.getDrawable(App.appContext, resId)

/**
 * 根据资源id获取图片
 */
fun getDrawableWithBounds(resId: Int): Drawable? {
    val drawable = ContextCompat.getDrawable(App.appContext, resId)
    drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    return drawable
}