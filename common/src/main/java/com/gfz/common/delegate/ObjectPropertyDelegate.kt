package com.gfz.common.delegate

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 *
 * created by xueya on 2022/2/21
 */
private class ObjectPropertyDelegate<T>(
    val beforeChange: ((T?) -> Unit)? = null,
    val afterChange: ((T?) -> Unit)? = null
) : ReadWriteProperty<Any, T?> {
    private var value: T? = null

    override operator fun getValue(thisRef: Any, property: KProperty<*>): T? {
        return value
    }

    override operator fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        beforeChange?.invoke(value)
        this.value = value
        afterChange?.invoke(value)
    }
}