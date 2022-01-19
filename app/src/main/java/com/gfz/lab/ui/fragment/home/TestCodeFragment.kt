package com.gfz.lab.ui.fragment.home

import com.gfz.common.utils.TimeCell
import com.gfz.lab.databinding.FragmentCodeBinding
import com.gfz.lab.ui.base.BaseVBFragment

/**
 *
 * created by gaofengze on 2021/10/12
 */
class TestCodeFragment : BaseVBFragment<FragmentCodeBinding>() {

    val timeCell: TimeCell by lazy {
        TimeCell()
    }

    override fun initView() {

    }
}