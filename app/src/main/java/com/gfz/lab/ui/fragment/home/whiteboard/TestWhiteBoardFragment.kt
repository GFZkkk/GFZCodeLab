package com.gfz.lab.ui.fragment.home.whiteboard

import android.graphics.Matrix
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.graphics.values
import com.gfz.common.utils.ToastUtil
import com.gfz.common.utils.TopLog
import com.gfz.lab.R
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

    private var whiteBoard: LocalWhiteBoardView? = null

    private val scaleUtil by lazy {
        ViewScaleUtil(binding.llWhiteBoardLayout, 1f, 3f)
    }

    val maxScale = 3f
    val minScale = 1f

    override fun initView() {
        with(binding) {

            binding.llWhiteBoardLayout.removeAllViews()
            binding.llWhiteBoardLayout.addView(View(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    200,
                    200
                )
                setBackgroundResource(R.color.black)
                setOnClickListener {
                    ToastUtil.showToast("点击")
                }
            })

            flWhiteBoardLayout.init { x, y, scale ->
                scaleUtil.onScale(x, y, scale)
                TopLog.e("testccc  scale:$scale")
                matrixView.update {
                    val curScale = values()[Matrix.MSCALE_X]
                    val newScale = curScale * scale

                    if (newScale > maxScale) {
                        postScale(maxScale / curScale, maxScale / curScale, x, y)
                    } else if (newScale < minScale) {
                        postScale(minScale / curScale, minScale / curScale, x, y)
                    } else {
                        postScale(scale, scale, x, y)
                    }

                }
            }

//            whiteBoard = createWhiteBoard()

//            addWhiteBoard(whiteBoard!!)

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
        binding.llWhiteBoardLayout.removeAllViews()
        view.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        binding.llWhiteBoardLayout.addView(view)
    }
}