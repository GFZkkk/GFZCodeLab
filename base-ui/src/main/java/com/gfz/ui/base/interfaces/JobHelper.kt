package com.gfz.ui.base.interfaces

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 *
 * created by xueya on 2022/2/24
 */
interface JobHelper {
    fun getScope(): CoroutineScope

    fun startJob(
        loading: Boolean = false,
        tag: Int = -1,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: ((Boolean) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job

    fun startSingleJob(
        loading: Boolean = false,
        tag: Int = 0,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: ((Boolean) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    )

    fun reStartSingleJob(
        loading: Boolean = false,
        tag: Int = 0,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: ((Boolean) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    )

    fun stopSingleJob(tag: Int = 0)
    fun showLoading(tag: Int)
    fun hideLoading(tag: Int)
    fun changeLoadingStatus(show: Boolean)
}