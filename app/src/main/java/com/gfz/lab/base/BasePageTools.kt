package com.gfz.lab.base

import android.os.Bundle

/**
 *
 * created by gaofengze on 2021/5/13
 */
interface BasePageTools {
    fun start(baseActivity: BaseActivity, bundle: Bundle? = null)
    fun start(baseFragment: BaseFragment, bundle: Bundle? = null)
    fun startForResult(baseActivity: BaseActivity, bundle: Bundle? = null, request: Int)
    fun startForResult(baseFragment: BaseFragment, bundle: Bundle? = null, request: Int)
    fun showToast(text: String)
    fun showToast(textRes: Int)
    fun fastClick(tag: Int = 0, dur: Int = 500): Boolean
}