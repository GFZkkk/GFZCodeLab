package com.gfz.common.ext

import com.gfz.common.utils.TopLog
import java.lang.reflect.ParameterizedType


fun Any.toLog(type: Int = TopLog.E, tag: String? = null) {
    TopLog.printLog(type, tag, this)
}

fun Any.getClass(index: Int): Class<*>? {
    var entityClass: Class<*>? = null
    val genericSuperclass = javaClass.genericSuperclass
    if (genericSuperclass is ParameterizedType) {
        val actualTypeArguments = genericSuperclass
            .actualTypeArguments
        if (actualTypeArguments.size > index) {
            entityClass = actualTypeArguments[index] as Class<*>
        }
    }
    return entityClass
}