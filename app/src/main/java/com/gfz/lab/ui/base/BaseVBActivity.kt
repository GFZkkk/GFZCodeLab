package com.gfz.lab.ui.base

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.gfz.lab.databinding.ActivityRootBinding
import com.gfz.lab.utils.TopLog
import java.lang.reflect.ParameterizedType

abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {

    private var _binding: VB? = null

    val binding: VB get() = _binding!!

    override fun loadView() {
        _binding = onCreateViewBinding()
        requireNotNull(_binding) { "binding不能为null" }
        setContentView(binding.root)
    }

    open fun onCreateViewBinding(): VB? {
        return getVBClass(0)!!.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
    }

    fun getVBClass(index: Int): Class<*>? {
        var entitiClass: Class<*>? = null
        val genericSuperclass = javaClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val actualTypeArguments = genericSuperclass
                .actualTypeArguments
            if (actualTypeArguments.size > index) {
                entitiClass = actualTypeArguments[index] as Class<*>
            }
        }
        return entitiClass
    }
}