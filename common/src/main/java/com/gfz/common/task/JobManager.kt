package com.gfz.common.task

import android.os.Message
import android.util.SparseArray
import android.util.SparseIntArray
import com.gfz.common.ext.launchSafe
import com.gfz.common.utils.TopLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * Created by xueya on 2022/1/28
 */
class JobManager(private val helper: JobHelper) {
    private val jobArray: SparseArray<Job> by lazy {
        SparseArray()
    }

    private val loadingArray: SparseIntArray by lazy {
        SparseIntArray()
    }

    fun reStartSingleJob(
        loading: Boolean = false,
        tag: Int = 0,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: ((Boolean) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        stopJob(tag)
        startSingleJob(loading, tag, onError, onComplete, block)
    }

    fun startJob(
        loading: Boolean = false,
        tag: Int = -1,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: ((Boolean) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        if (loading) {
            showLoading(tag)
        }

        val job = getJob(onError, block){
            if (loading) {
                hideLoading(tag)
            }
            onComplete?.invoke(it)
        }
    }

    fun startSingleJob(
        loading: Boolean = false,
        tag: Int = 0,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: ((Boolean) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        if (jobArray.indexOfKey(tag) > 0) {
            TopLog.e("$tag 正在执行")
            return
        }

        if (loading) {
            showLoading(tag)
        }

        val job = getJob(onError, block){
            jobArray.remove(tag)
            if (loading) {
                hideLoading(tag)
            }
            onComplete?.invoke(it)
        }

        jobArray.append(tag, job)

    }

    fun stopJob(tag: Int = 0) {
        jobArray[tag]?.let {
            it.cancel()
            jobArray.remove(tag)
        }
    }

    private fun getJob(
        onError: ((Throwable) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit,
        onComplete: ((Boolean) -> Unit)
    ): Job {
        return helper.getScope()
            .launchSafe(
                onError = onError,
                onComplete = onComplete,
                block = block
            )
    }

    fun showLoading(tag: Int) {
        val num = loadingArray[tag, 0]
        loadingArray.append(tag, num + 1)
        checkLoadingStatus()
    }

    fun hideLoading(tag: Int) {
        val num = loadingArray[tag, 0]
        if (num - 1 <= 0) {
            loadingArray.delete(tag)
        } else {
            loadingArray.append(tag, num)
        }
        checkLoadingStatus()
    }

    private fun checkLoadingStatus() {
        helper.changeLoadingStatus(loadingArray.size() > 0)
    }
}

interface JobHelper {
    fun getScope(): CoroutineScope
    fun startJob(
        loading: Boolean = false,
        tag: Int = -1,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: ((Boolean) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    )
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
    fun stopJob(tag: Int = 0)
    fun showLoading(tag: Int)
    fun hideLoading(tag: Int)
    fun changeLoadingStatus(show: Boolean)
}

data class JobItem(
    val tag: Int = 0,
    val loading: Boolean = false,
    private val onError: ((Throwable) -> Unit)? = null,
    private val onComplete: ((Boolean) -> Unit)? = null,
    private val block: suspend CoroutineScope.() -> Unit
) {
    val scopeCallBack: ScopeCallBack by lazy {
        ScopeCallBack(onError, onComplete, block)
    }
}

data class ScopeCallBack(
    val onError: ((Throwable) -> Unit)? = null,
    var onComplete: ((Boolean) -> Unit)? = null,
    val block: suspend CoroutineScope.() -> Unit
)