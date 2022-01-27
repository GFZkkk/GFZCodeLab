package com.gfz.common.ext

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.*
import kotlin.reflect.KClass

/**
 * Created by xueya on 2022/1/27
 */
fun <T> MutableLiveData<T>.toLiveData(): LiveData<T>{
    return this
}