package com.gfz.whiteboard.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import coil.clear
import coil.load
import coil.request.Disposable

/**
 *
 * created by xueya on 2022/10/26
 */
class WhiteBoardImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), WhiteBoardImage {
    private var scale = 1.0
    private val boardWidth = 10000
    private val boardHeight = 16000

    private var imageUrl: String = ""
    private var disposable: Disposable? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)

        val wScale = 1.0 * w / boardWidth
        val hScale = 1.0 * h / boardHeight
        scale = wScale.coerceAtMost(hScale)
        val realWidth = (boardWidth * scale).toInt()
        val realHeight = (boardHeight * scale).toInt()

        setMeasuredDimension(realWidth, realHeight)
    }

    override fun setImage(url: String) {
        this.imageUrl = url
        clear()
        loadImage()
    }

    private fun loadImage() {
        if (imageUrl.isEmpty()) {
            return
        }
        disposable = load(imageUrl) {
            allowHardware(false)
        }
    }

    fun destroy() {
        disposable?.dispose()
        disposable = null
    }
}

interface WhiteBoardImage {
    fun setImage(url: String)
}