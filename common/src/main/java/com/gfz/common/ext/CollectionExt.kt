package com.gfz.common.ext

/**
 *
 * created by xueya on 2022/7/7
 */
inline  fun <T> MutableList<T>.removeIf(filter: T?.() -> Boolean): Boolean{
    val it = iterator()
    var removed = false
    while (it.hasNext()){
        if (filter(it.next())){
            it.remove()
            removed = true
        }
    }
    return removed
}