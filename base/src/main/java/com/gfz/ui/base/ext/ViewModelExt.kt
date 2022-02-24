package com.gfz.ui.base.ext

import androidx.lifecycle.*

/**
 * Created by xueya on 2022/1/27
 */
fun <T> MutableLiveData<T>.asLiveData(): LiveData<T>{
    return this
}