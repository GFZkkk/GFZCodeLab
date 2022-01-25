package com.gfz.lab.ui.base

import android.os.Bundle
import androidx.annotation.IdRes

/**
 *
 * created by gaofengze on 2021/5/13
 */
interface BasePageTools {
    fun start(activity: Class<out BaseActivity>, bundle: Bundle? = null)
    fun start(@IdRes action: Int, bundle: Bundle? = null)
    fun pop()
    fun popTo(@IdRes action: Int, inclusive: Boolean)
    fun showToast(text: String)
    fun showToast(textRes: Int)
    fun fastClick(tag: Int = 0, dur: Int = 500): Boolean
    fun addIdleTask(keep: Boolean = false, block: () -> Unit)
}