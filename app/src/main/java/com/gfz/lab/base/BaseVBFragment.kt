package com.gfz.lab.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.gfz.common.ext.getClass

abstract class BaseVBFragment<VB : ViewBinding> : BaseFragment() {

    private var _binding: VB? = null
    val binding: VB
        get() = _binding!!

    var needSaveView = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = getViewBinding(inflater, container)
        }
        requireNotNull(_binding) {
            "ViewBinding == null"
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!needSaveView) {
            _binding = null
        }
    }

    /**
     * 默认通过反射获取 ViewBinding
     */
    private fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB? {
        return getViewBindingClass()?.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )?.invoke(null, inflater, container, false) as? VB
    }

    /**
     * 获取泛型中的ViewBinding的类型
     */
    private fun getViewBindingClass(): Class<*>? {
        return getClass(getViewBindingTypePosition())
    }

    /**
     * 获取泛型中ViewBinding的位置
     */
    open fun getViewBindingTypePosition() = 0
}