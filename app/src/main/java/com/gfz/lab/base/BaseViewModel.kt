package com.gfz.lab.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gfz.common.ext.asLiveData
import com.gfz.common.task.JobHelper
import com.gfz.common.task.JobItem
import com.gfz.common.task.JobManager
import kotlinx.coroutines.CoroutineScope


abstract class BaseViewModel : ViewModel(), JobHelper {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading = _isLoading.asLiveData()

    private val jobManager: JobManager by lazy {
        JobManager(this)
    }

    fun showLoading(show: Boolean) {
        _isLoading.value = show
    }

    override fun onCleared() {
        super.onCleared()
    }

    override fun startJob(
        loading: Boolean,
        tag: Int,
        onError: ((Throwable) -> Unit)?,
        onComplete: ((Boolean) -> Unit)?,
        block: suspend CoroutineScope.() -> Unit
    ) {
        jobManager.startJob(loading, tag, onError, onComplete, block)
    }

    override fun startSingleJob(
        loading: Boolean,
        tag: Int,
        onError: ((Throwable) -> Unit)?,
        onComplete: ((Boolean) -> Unit)?,
        block: suspend CoroutineScope.() -> Unit
    ) {
        jobManager.startSingleJob(loading, tag, onError, onComplete, block)
    }

    override fun reStartSingleJob(
        loading: Boolean,
        tag: Int,
        onError: ((Throwable) -> Unit)?,
        onComplete: ((Boolean) -> Unit)?,
        block: suspend CoroutineScope.() -> Unit
    ) {
        jobManager.reStartSingleJob(loading, tag, onError, onComplete, block)
    }

    override fun stopJob(tag: Int) {
        jobManager.stopJob(tag)
    }

    override fun showLoading(tag: Int) {
        jobManager.showLoading(tag)
    }

    override fun hideLoading(tag: Int) {
        jobManager.hideLoading(tag)
    }

    override fun changeLoadingStatus(show: Boolean) {
        showLoading(show)
    }

    override fun getScope(): CoroutineScope {
        return viewModelScope
    }
}