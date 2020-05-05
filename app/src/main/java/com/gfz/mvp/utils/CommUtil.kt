package com.gfz.mvp.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.gfz.mvp.data.App

/**
 * 根据资源id获取颜色
 */
fun getmColor(resId: Int) = ContextCompat.getColor(App.appContext, resId)

/**
 * 根据资源id获取图片
 */
fun getmDrawable(resId: Int): Drawable? = ContextCompat.getDrawable(App.appContext, resId)

/**
 * 根据资源id获取图片
 */
fun getDrawableWithBounds(resId: Int): Drawable? {
    val drawable = ContextCompat.getDrawable(App.appContext, resId)
    drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    return drawable
}

/**
 * 设置控件显隐
 */
fun View?.setDisplay(visible: Boolean) {
    this?.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View?.setVisible(visible: Boolean) {
    this?.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

/**
 * 某个view是否显示
 */
fun View?.isDisplay(): Boolean = this?.visibility == View.VISIBLE

/**
 * 根据手机的分辨率从 dx(像素) 的单位 转成为 px
 */
fun Int.toPX(context: Context = App.appContext): Int = (this * context.resources.displayMetrics.density + 0.5f).toInt()

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun Int.toDP(context: Context = App.appContext): Int = (this / context.resources.displayMetrics.density + 0.5f).toInt()

fun getCamelCase(underScoreCase: String): String? {
    val str = underScoreCase.split("_").toTypedArray()
    val stringBuilder = StringBuilder()
    for (i in str.indices) {
        val s = str[i]
        if (s.isNotEmpty()) {
            if (i == 0) {
                stringBuilder.append(s)
            } else {
                stringBuilder.append(stringChange(s))
            }
        }
    }
    return stringBuilder.toString()
}

fun stringChange(s: String): String? {
    val c = s.toCharArray()
    for (i in s.indices) {
        if (i == 0) {
            if (c[i] in 'a'..'z') {
                c[i] = Character.toUpperCase(c[i])
            }
        } else {
            if (c[i] in 'A'..'Z') {
                c[i] = Character.toLowerCase(c[i])
            }
        }
    }
    return String(c)
}

