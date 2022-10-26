package com.gfz.lab.ui.fragment.home.whiteboard.view

import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.ui.graphics.Path

/**
 *
 * created by xueya on 2022/10/25
 */
class WhiteBoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback, Runnable {

    private val mSurfaceHolder: SurfaceHolder = holder
    private var mDrawing = false

    private val path = Path()

    init {
        mSurfaceHolder.addCallback(this)
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT)
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        mDrawing = true
        Thread(this).start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mDrawing = false
    }

    override fun run() {
        while (mDrawing) {
            drawBoard()
        }
    }

    private fun drawBoard() {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return true
    }
}