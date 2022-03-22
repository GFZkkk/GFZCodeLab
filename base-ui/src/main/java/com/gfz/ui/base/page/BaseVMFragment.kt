package com.gfz.ui.base.page

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * Created by xueya on 2022/1/27
 */
abstract class BaseVMFragment<VB : ViewBinding, VM : BaseViewModel> : BaseVBFragment<VB>() {
    abstract val viewModel: VM

    override fun init(view: View, savedInstanceState: Bundle?) {
        super.init(view, savedInstanceState)
        viewModel.isLoading.observe(this){
            showLoading(it)
        }
        initObserver()
        initData()
    }

    abstract fun initData()

    abstract fun initObserver()
}