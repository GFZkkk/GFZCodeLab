package com.gfz.common.ext

import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import java.lang.ref.WeakReference


fun <T : View> T.postTask(delay: Long = 0, task: T.() -> Unit) {
    postDelayed(object : ViewRunnable<T>(WeakReference(this)) {
        override fun run(view: T) {
            task(view)
        }
    }, delay)
}

abstract class ViewRunnable<T : View>(private val view: WeakReference<T>) : Runnable {
    override fun run() {
        view.get()?.let {
            run(it)
        }
    }

    abstract fun run(view: T)
}

const val LEFT: Int = 0
const val TOP: Int = 1
const val RIGHT: Int = 2
const val BOTTOM: Int = 3

fun TextView.setIcon(
    @DrawableRes leftId: Int = 0,
    @DrawableRes topId: Int = 0,
    @DrawableRes rightId: Int = 0,
    @DrawableRes bottomId: Int = 0
) {
    val left = context.getDrawableWithBounds(leftId)
    val top = context.getDrawableWithBounds(topId)
    val right = context.getDrawableWithBounds(rightId)
    val bottom = context.getDrawableWithBounds(bottomId)
    setCompoundDrawables(left, top, right, bottom)
}

fun TextView?.clearIcon() {
    this?.setCompoundDrawables(null, null, null, null)
}

/**
 * 设置TextView颜色资源
 */
fun TextView.setColorRes(
    resId: Int
) {
    this.setTextColor(context.getCompatColor(resId))
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

fun View.setHeight(height: Int) {
    val params = layoutParams
    params.height = height
    layoutParams = params
}

fun View.setWidth(width: Int) {
    val params = layoutParams
    params.width = width
    layoutParams = params
}
