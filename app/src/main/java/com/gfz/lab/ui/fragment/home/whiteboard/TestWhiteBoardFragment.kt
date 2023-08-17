package com.gfz.lab.ui.fragment.home.whiteboard

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gfz.lab.databinding.FragmentWhiteboardBinding
import com.gfz.lab.ui.fragment.home.whiteboard.view.LocalWhiteBoardView
import com.gfz.lab.ui.fragment.home.whiteboard.view.WhiteBoardView
import com.gfz.lab.utils.ViewScaleUtil
import com.gfz.ui.base.page.BaseVBFragment

/**
 *
 * created by xueya on 2022/10/25
 */
class TestWhiteBoardFragment : BaseVBFragment<FragmentWhiteboardBinding>() {

    var whiteBoard: LocalWhiteBoardView? = null

    private val scaleUtil by lazy {
        ViewScaleUtil(binding.llWhiteBoardLayout, 1f, 3f)
    }

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
            flWhiteBoardLayout.init { fl, fl2, fl3 ->
                scaleUtil.onScale(fl, fl2, fl3)
            }
        }
    }

    private fun createWhiteBoard(): LocalWhiteBoardView {
        return LocalWhiteBoardView(requireContext())
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