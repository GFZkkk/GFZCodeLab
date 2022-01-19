package com.gfz.lab.ext

import android.content.Context
import com.gfz.common.ext.getCompatColor
import com.gfz.lab.app.KTApp
import com.gfz.lab.ui.base.BaseFragment

fun BaseFragment.getColor(resId: Int) = context?.getCompatColor(resId)

/**
 * 根据手机的分辨率从 dx(像素) 的单位 转成为 px
 */
fun Int.toPX(context: Context = KTApp.appContext): Int = (this * context.resources.displayMetrics.density + 0.5f).toInt()

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun Int.toDP(context: Context = KTApp.appContext): Int = (this / context.resources.displayMetrics.density + 0.5f).toInt()