package com.gfz.lab.ui.fragment.home

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gfz.common.utils.ViewScaleManager
import com.gfz.lab.databinding.FragmentWhiteboardBinding
import com.gfz.ui.base.page.BaseVBFragment

/**
 *
 * created by xueya on 2022/10/25
 */
class TestWhiteBoardFragment : BaseVBFragment<FragmentWhiteboardBinding>() {

    private val scaleUtil by lazy {
        ViewScaleManager(requireContext(), minScale, maxScale, enableMove = false)
    }

    val maxScale = 3f
    val minScale = 1f

    override fun initView() {
        with(binding) {
        }
    }

    private fun addWhiteBoard(view: View) {
        binding.llWhiteBoardLayout.removeAllViews()
        view.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        binding.llWhiteBoardLayout.addView(view)
    }
}