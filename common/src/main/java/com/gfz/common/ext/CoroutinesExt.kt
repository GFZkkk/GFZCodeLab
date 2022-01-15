package com.gfz.common.ext

import com.gfz.common.utils.TopLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Created by gaofengze on 2022/1/15
 */
fun CoroutineScope.launchSafe(
    context: CoroutineContext = EmptyCoroutineContext,
    onError: ((Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
): Job {
    var c = context
    if (context[CoroutineExceptionHandler.Key] == null) {
        c = context + defaultExceptionHandler(onError)
    }
    return launch(c) {
        try {
            block()
        } catch (e: Exception) {
            TopLog.e(e)
            onError?.invoke(e)
        }
    }
}

fun defaultExceptionHandler(onError: ((Throwable) -> Unit)? = null) =
    CoroutineExceptionHandler { _, exception ->
        TopLog.e(exception)
        onError?.invoke(exception)
    }