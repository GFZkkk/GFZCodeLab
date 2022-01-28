package com.gfz.lab.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gfz.common.ext.asLiveData
import com.gfz.common.task.JobHelper
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

    override fun changeLoadingStatus(show: Boolean) {
        showLoading(show)
    }

    override fun getScope(): CoroutineScope {
        return viewModelScope
    }

    override fun startJob(tag: Int, loading: Boolean, block: suspend () -> Unit) {
        jobManager.startJob(tag, loading, block)
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
}