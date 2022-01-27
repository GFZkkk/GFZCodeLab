package com.gfz.lab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gfz.common.ext.toLiveData

abstract class BaseViewModel : ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading = _isLoading.toLiveData()

    fun showLoading(show: Boolean){
        _isLoading.value = show
    }

}