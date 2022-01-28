package com.gfz.lab.ui.fragment.home

import com.gfz.lab.databinding.FragmentAnimationBinding
import com.gfz.lab.base.BaseVBFragment

/**
 * Created by gaofengze on 2020/9/15.
 */
class TestAnimationFragment : BaseVBFragment<FragmentAnimationBinding>() {

    override fun initView() {
        with(binding) {
//            fw.start()
            fw.setOnClickListener {
                fw.start()
            }
            interpolatorView.start()
        }
    }

    override fun getTitleText(): String {
        return "自定义动画实验区"
    }
}