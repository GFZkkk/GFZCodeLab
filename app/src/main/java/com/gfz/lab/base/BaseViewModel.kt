package com.gfz.lab.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gfz.common.ext.launchSafe
import com.gfz.common.ext.asLiveData

abstract class BaseViewModel : ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading = _isLoading.asLiveData()

    fun showLoading(show: Boolean){
        _isLoading.value = show
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun startJob(loading: Boolean = false, block: suspend () -> Unit){
        viewModelScope.launchSafe {
            block()
        }
    }
}