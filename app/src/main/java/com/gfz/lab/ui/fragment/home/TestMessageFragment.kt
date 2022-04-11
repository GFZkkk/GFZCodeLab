package com.gfz.lab.ui.fragment.home

import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.gfz.common.ext.launchSafe
import com.gfz.common.task.RecyclerPool
import com.gfz.gif.GifUtil
import com.gfz.lab.databinding.FragmentMessageBinding
import com.gfz.message.IpUtil
import com.gfz.ui.base.page.BaseFragment
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