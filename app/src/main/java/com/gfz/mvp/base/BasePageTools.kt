package com.gfz.mvp.base

/**
 *
 * created by gaofengze on 2021/5/13
 */
interface BasePageTools {
    fun showToast(text: String)
    fun showToast(textRes: Int)
    fun fastClick(tag: Int = 0, dur: Int = 500): Boolean
}