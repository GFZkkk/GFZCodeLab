package com.gfz.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
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
            @Suppress("UNCHECKED_CAST")
            _binding = getClass(0)!!.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
                .invoke(null, inflater, container, false) as VB
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!needSaveView) {
            _binding = null
        }
    }
}