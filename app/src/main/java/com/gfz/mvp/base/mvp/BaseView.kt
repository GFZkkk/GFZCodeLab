package com.gfz.mvp.base.mvp

/**
 * created by gaofengze on 2020-01-19
 */

interface BaseView {
    fun showLoading()

    fun hideLoading()

    fun showError(msg : String)
}