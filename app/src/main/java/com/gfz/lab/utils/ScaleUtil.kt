package com.gfz.lab.utils

import android.view.View
import kotlin.math.abs

/**
 * 缩放工具，支持任意坐标系缩放
 * created by xueya on 2023/3/7
 */
class ViewScaleUtil(
    private val view: View,
    private val minScale: Float = 1f,
    private val maxScale: Float = 3f
) : ScaleUtil() {

    override val width: Int
        get() = view.run {
            width
        }.toInt()
    override val height: Int
        get() = view.run {
            height
        }.toInt()

    fun onScale(focusX: Float, focusY: Float, scaleFactor: Float) {
        val newScale = (view.scaleX * scaleFactor).coerceIn(minScale, maxScale)
        scale(focusX, focusY, view.scaleX, newScale) { fx, fy ->
            view.apply {
                scaleX = newScale
                scaleY = newScale
                translationX = fx
                translationY = fy
            }
        }
    }

    fun onMove(dx: Float, dy: Float): Boolean {
        val maxOffsetX = width * (view.scaleX - 1) / 2
        val maxOffsetY = height * (view.scaleY - 1) / 2
        val curOffsetX = view.translationX
        val curOffsetY = view.translationY
        val newOffsetX = curOffsetX + dx
        val newOffsetY = curOffsetY + dy

        val horizontalScroll = abs(dx) > abs(dy)
        val onLeftBound = curOffsetX == -maxOffsetX
        val onRightBound = curOffsetX == maxOffsetX
        if (horizontalScroll && ((onLeftBound && dx < 0) || (onRightBound && dx > 0))) {
            return false
        }
        view.apply {
            translationX = newOffsetX.coerceIn(-maxOffsetX, maxOffsetX)
            translationY = newOffsetY.coerceIn(-maxOffsetY, maxOffsetY)
        }
        return true
    }

    override fun getCurOffset(curScale: Float, curOffsetCallback: (Float, Float) -> Unit) {
        val offsetX = -view.translationX + width * (curScale - 1) / 2
        val offsetY = -view.translationY + height * (curScale - 1) / 2
        curOffsetCallback.invoke(offsetX / curScale, offsetY / curScale)
    }

    override fun getPointByNewOffset(
        newScale: Float,
        newOffsetX: Float,
        newOffsetY: Float,
        newOffsetCallback: (Float, Float) -> Unit
    ) {
        val maxOffsetX = width * (newScale - 1) / 2
        val maxOffsetY = height * (newScale - 1) / 2
        val offsetX = maxOffsetX - newOffsetX * newScale
        val offsetY = maxOffsetY - newOffsetY * newScale
        newOffsetCallback.invoke(
            offsetX.coerceIn(-maxOffsetX, maxOffsetX),
            offsetY.coerceIn(-maxOffsetY, maxOffsetY)
        )
    }
}

abstract class ScaleUtil {

    protected open val width: Int = 0
    protected open val height: Int = 0

    // 获取当前偏移
    abstract fun getCurOffset(curScale: Float, curOffsetCallback: (Float, Float) -> Unit)

    // 根据新的偏移获取坐标
    abstract fun getPointByNewOffset(
        newScale: Float,
        newOffsetX: Float,
        newOffsetY: Float,
        newOffsetCallback: (Float, Float) -> Unit
    )

    // 获取缩放后的坐标
    fun scale(
        x: Float,
        y: Float,
        curScale: Float,
        newScale: Float,
        result: (Float, Float) -> Unit
    ) {
        getCurOffset(curScale) { offsetX, offsetY ->
            getAfterScaleOffset(
                x,
                y,
                offsetX,
                offsetY,
                curScale,
                newScale
            ) { newOffsetX, newOffsetY ->
                getPointByNewOffset(newScale, newOffsetX, newOffsetY) { fx, fy ->
                    result(fx, fy)
                }
            }
        }
    }

    // 获取缩放后的偏移
    private fun getAfterScaleOffset(
        x: Float,
        y: Float,
        offsetX: Float,
        offsetY: Float,
        curScale: Float,
        newScale: Float,
        result: (Float, Float) -> Unit
    ) {
        // 缩放前的大小
        val w = width / curScale
        val h = height / curScale
        // 缩放中心在屏幕的百分比
        val scaleXCenter = x / width
        val scaleYCenter = y / height
        // 缩放中心距离左上角的距离
        val scaleXOffset = w * scaleXCenter
        val scaleYOffset = h * scaleYCenter
        // 缩放中心坐标
        val scaleX = offsetX + scaleXOffset
        val scaleY = offsetY + scaleYOffset

        // 缩放后的大小
        val newW = width / newScale
        val newH = height / newScale
        // 缩放后的缩放中心的偏移
        val newScaleXOffset = newW * scaleXCenter
        val newScaleYOffset = newH * scaleYCenter
        // 缩放后的缩放中心坐标
        val newScaleX = offsetX + newScaleXOffset
        val newScaleY = offsetY + newScaleYOffset
        // 缩放后的缩放中心坐标
        val diffX = scaleX - newScaleX
        val diffY = scaleY - newScaleY
        // 新的偏移
        val newOffsetX = offsetX + diffX
        val newOffsetY = offsetY + diffY
        result(newOffsetX, newOffsetY)
    }

}