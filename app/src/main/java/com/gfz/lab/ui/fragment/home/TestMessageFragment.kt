package com.gfz.lab.ui.fragment.home

import com.gfz.lab.databinding.FragmentMessageBinding
import com.gfz.message.utils.IpUtil
import com.gfz.ui.base.page.BaseVBFragment

/**
 *
 * created by xueya on 2022/4/11
 */
class TestMessageFragment() : BaseVBFragment<FragmentMessageBinding>() {
    override fun initView() {
        binding.tvIpAddress.text = IpUtil.getIpAddress()
    }

    override fun getTitleText(): String {
        return "消息列表实验区"
    }
}