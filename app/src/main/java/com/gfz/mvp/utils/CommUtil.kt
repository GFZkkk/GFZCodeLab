package com.gfz.mvp.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.gfz.mvp.data.KTApp
import java.util.*

/**
 * 根据资源id获取颜色
 */
fun Context.getCompatColor(resId: Int) = ContextCompat.getColor(this, resId)

/**
 * 根据资源id获取图片
 */
fun Context.getCompatDrawable(resId: Int) = ContextCompat.getDrawable(this, resId)

/**
 * 根据资源id获取图片
 */
fun Context.getDrawableWithBounds(resId: Int): Drawable? {
    val drawable = getCompatDrawable(resId)
    drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    return drawable
}

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

/**
 * 根据手机的分辨率从 dx(像素) 的单位 转成为 px
 */
fun Int.toPX(context: Context = KTApp.appContext): Int = (this * context.resources.displayMetrics.density + 0.5f).toInt()

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun Int.toDP(context: Context = KTApp.appContext): Int = (this / context.resources.displayMetrics.density + 0.5f).toInt()

fun getLowerCamelCase(str: String): String{
    val word = str.toLowerCase(Locale.getDefault()).split("_")
    val result = StringBuilder()
    word.forEach {
        if(it.isNotBlank()){
            if (result.isNotBlank()){
                result.append(it[0] - 32)
                result.append(it.substring(1))
            }else{
                result.append(it)
            }
        }
    }
    return result.toString()
}

fun Any.toLog(type: Int = 5, tag: String? = null){
    TopLog.printLog(type, tag, this)
}


