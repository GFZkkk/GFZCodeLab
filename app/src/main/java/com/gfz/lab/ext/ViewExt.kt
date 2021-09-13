package com.gfz.lab.ext

import android.view.View
import android.widget.TextView

const val LEFT: Int = 0
const val TOP: Int = 1
val RIGHT: Int = 2
val BOTTOM: Int = 3

/**
 * 设置TextView文字旁边的图标
 * 0：文字左边图标
 * 1：文字上边图标
 * 2：文字右边图标
 * 3：文字下边图标
 */
fun TextView?.setIcon(
    resId: Int,
    direction: Int
) {
    when (direction) {
        LEFT -> this?.setCompoundDrawables(context.getDrawableWithBounds(resId), null, null, null)
        TOP -> this?.setCompoundDrawables(null, context.getDrawableWithBounds(resId), null, null)
        RIGHT -> this?.setCompoundDrawables(null, null, context.getDrawableWithBounds(resId), null)
        BOTTOM -> this?.setCompoundDrawables(null, null, null, context.getDrawableWithBounds(resId))
        else -> {
            this?.setCompoundDrawables(null, null, null, null)
        }
    }
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