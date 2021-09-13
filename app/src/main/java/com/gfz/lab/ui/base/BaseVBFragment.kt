package com.gfz.lab.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseVBFragment<VB : ViewBinding> : BaseFragment() {

    private var _binding: VB? = null
    val binding: VB get() = _binding!!
    var needSaveView = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroyView() {
                    if (!needSaveView){
                        _binding = null
                    }
                }
            })
            @Suppress("UNCHECKED_CAST")
            _binding = getVBClass(0)!!.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
                .invoke(null, inflater, container, false) as VB
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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