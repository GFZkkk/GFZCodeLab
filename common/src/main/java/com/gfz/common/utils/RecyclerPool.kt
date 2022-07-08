package com.gfz.common.utils

import android.graphics.Bitmap
import android.os.Handler
import android.util.SparseArray
import androidx.core.util.forEach
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.gfz.common.loop.TimeLoop
import java.util.*
import kotlin.math.max

/**
 * 避免重复创建对象的复用池
 * created by xueya on 2022/3/28
 */
class RecyclerPool(lifecycle: Lifecycle? = null, private val autoRelease: Boolean = true) {
    val pool = SparseArray<Any>()
    var maxKey = 0

    init {
        lifecycle?.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                clearAll()
            }
        })
    }

    inline fun <reified T> put(create: () -> T) {
        put(getIncrementKey(), create)
    }

    inline fun <reified T> put(key: Int, create: () -> T): Pair<Int, T> {
        return key to create().apply {
            maxKey = max(maxKey, key)
            pool.append(key, this)
        }
    }

    inline fun <reified T> get(key: Int = 0, create: () -> T): T {
        val data: Any? = pool[key]
        return data as? T ?: put(key, create).second
    }

    inline fun <reified T> get(key: Int = 0): T? {
        val data: Any? = pool[key]
        return data as? T
    }

    fun getIncrementKey(): Int = ++maxKey

    fun clear(key: Int, release: Boolean = autoRelease){
        pool[key]?.let {
            if (release){
                release(it)
            }
            pool.remove(key)
        }
    }

    fun clearAll(release: Boolean = autoRelease){
        if (release){
            pool.forEach { _, value ->
                release(value)
            }
        }
        pool.clear()
    }

    private fun release(value : Any): Boolean {
        try {
            when (value) {
                is Bitmap -> value.recycle()
                is Timer -> value.cancel()
                is Handler -> value.removeCallbacksAndMessages(null)
                is TimeLoop -> value.remove()
            }
            return true
        } catch (e: Exception){
            TopLog.e(e)
        }
        return false
    }
}