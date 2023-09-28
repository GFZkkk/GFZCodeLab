package com.gfz.whiteboard

import android.content.Context
import androidx.lifecycle.LifecycleOwner

/**
 *
 * created by xueya on 2023/9/27
 */
interface LiveRoomOwner : LifecycleOwner {
    fun getContext(): Context

    fun runOnUIThread(block: () -> Unit)
}