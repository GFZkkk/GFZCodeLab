package com.gfz.lab.ui.fragment.home

import androidx.fragment.app.viewModels
import com.gfz.lab.databinding.FragmentFlowBinding
import com.gfz.lab.ui.base.BaseVBFragment
import com.gfz.lab.ui.base.BaseVMFragment
import com.gfz.lab.viewmodel.FlowViewModel

/**
 * Created by xueya on 2022/1/11
 */
class TestFlowFragment() : BaseVMFragment<FlowViewModel, FragmentFlowBinding>() {
    override val viewModel: FlowViewModel by viewModels()

    override fun initView() {
    }
}