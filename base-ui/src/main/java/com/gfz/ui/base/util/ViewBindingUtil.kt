package com.gfz.ui.base.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.gfz.common.base.BaseApplication

/**
 *
 * created by gaofengze on 2021/1/27
 */

inline fun <reified VB : ViewBinding> Activity.viewBind() = lazy {
    inflateBinding<VB>(layoutInflater).apply {
        setContentView(root)
    }
}

inline fun <reified VB : ViewBinding> Dialog.viewBind() = lazy {
    inflateBinding<VB>(layoutInflater).apply {
        setContentView(root)
    }
}

inline fun <reified VB : ViewBinding> viewBind(parent: ViewGroup): VB {
    val method = VB::class.java.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
    return method.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
}

inline fun <reified VB : ViewBinding> viewBind(context: Context = BaseApplication.appContext) =
    lazy<VB> {
        inflateBinding(LayoutInflater.from(context))
    }

@Suppress("UNCHECKED_CAST")
inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as VB