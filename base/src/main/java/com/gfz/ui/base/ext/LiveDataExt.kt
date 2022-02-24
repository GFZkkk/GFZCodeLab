package com.gfz.ui.base.ext

import android.os.Looper
import androidx.lifecycle.MutableLiveData

/**
 * livedata
 * created by gaofengze on 2022/2/10
 */

fun <T> MutableLiveData<T>.update(block: T.() -> Unit) {
    // normal
    val data = value?.apply(block)
    if (Thread.currentThread() == Looper.getMainLooper().thread) {
        value = data
    } else {
        postValue(data)
    }
    // sample
    // value = value?.apply(block)
}

fun <T> MutableLiveData<List<T>>.updateList(block: MutableList<T>.() -> Unit) {
    // normal
    val data = value?.toMutableList()?.apply(block)
    if (Thread.currentThread() == Looper.getMainLooper().thread) {
        value = data
    } else {
        postValue(data)
    }
    // sample
    // value = value?.toMutableList()?.apply(block)
}