package com.gfz.lab.ui.fragment.home.whiteboard

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gfz.lab.databinding.FragmentWhiteboardBinding
import com.gfz.lab.ui.fragment.home.whiteboard.view.LocalWhiteBoardView
import com.gfz.lab.ui.fragment.home.whiteboard.view.WhiteBoardView
import com.gfz.ui.base.page.BaseVBFragment

/**
 *
 * created by xueya on 2022/10/25
 */
class TestWhiteBoardFragment : BaseVBFragment<FragmentWhiteboardBinding>() {

    var whiteBoard: LocalWhiteBoardView? = null

    override fun initView() {
        with(binding) {
            whiteBoard = createWhiteBoard()

            addWhiteBoard(whiteBoard!!)

            btnPaint.setOnClickListener {
                whiteBoard?.setIsEraser(false)
            }

            btnRubber.setOnClickListener {
                whiteBoard?.setIsEraser(true)
            }
        }
    }

    private fun createWhiteBoard(): LocalWhiteBoardView {
        return LocalWhiteBoardView(requireContext())
    }

    private fun addWhiteBoard(view: View) {
        binding.flWhiteBoardLayout.removeAllViews()
        view.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        binding.flWhiteBoardLayout.addView(view)
    }
}