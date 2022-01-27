package com.gfz.lab.ui.base

import androidx.viewbinding.ViewBinding

/**
 * Created by xueya on 2022/1/27
 */
abstract class BaseVMFragment<VM : BaseViewModel, VB : ViewBinding> : BaseVBFragment<VB>() {
    abstract val viewModel: VM
}