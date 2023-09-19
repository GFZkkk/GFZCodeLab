package com.gfz.lab.ui.fragment.home.whiteboard.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.graphics.values
import com.gfz.common.utils.TopLog

/**
 *
 * created by xueya on 2023/8/30
 */
class MatrixView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val matrix: Matrix = Matrix()

    fun update(block: Matrix.() -> Unit) {
        block(matrix)
        TopLog.e("testccc  update scale:${matrix.values()[Matrix.MSCALE_X]}")
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.setMatrix(matrix)
    }
}