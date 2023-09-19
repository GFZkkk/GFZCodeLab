package com.gfz.lab.ui.fragment.home.whiteboard.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.FrameLayout

/**
 *
 * created by xueya on 2023/8/16
 */
class ScaleLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var onScale: (Float, Float, Float) -> Unit = { _, _, _ -> }

    private val mScaleGestureDetector by lazy {
        ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                onScale(detector.focusX, detector.focusY, detector.scaleFactor)
                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
            }
        })
    }

    fun init(onScale: (Float, Float, Float) -> Unit) {
        this.onScale = onScale
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 处理缩放
        mScaleGestureDetector.onTouchEvent(event)
        return true
    }
}