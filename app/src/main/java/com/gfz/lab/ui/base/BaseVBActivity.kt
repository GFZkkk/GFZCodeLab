package com.gfz.lab.ui.base

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.gfz.common.ext.getClass

abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {

    private var _binding: VB? = null

    val binding: VB get() = _binding!!

    override fun loadView() {
        _binding = onCreateViewBinding()
        requireNotNull(_binding) { "binding不能为null" }
        setContentView(binding.root)
    }

    open fun onCreateViewBinding(): VB? {
        @Suppress("UNCHECKED_CAST")
        return getClass(0)!!.getMethod("inflate", LayoutInflater::class.java)
            .invoke(null, layoutInflater) as VB
    }
}