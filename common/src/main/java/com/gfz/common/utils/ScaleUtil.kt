package com.gfz.common.utils

import android.content.Context
import android.view.*
import android.widget.Scroller
import kotlin.math.abs

/**
 * 缩放工具，支持任意坐标系缩放
 * created by xueya on 2023/3/7
 */

class ViewScaleManager(
    private val context: Context,
    private val minScale: Float = 1f,
    private val maxScale: Float = 3f,
    var enableScale: Boolean = false,
    var enableMove: Boolean = false,
    view: View? = null,
    listener: View.OnClickListener? = null,
) : ScaleGestureDetector.OnScaleGestureListener, GestureDetector.SimpleOnGestureListener() {

    // 0:VERTICAL 1:HORIZONTAL
    var scrollMode: Int = 0
        set(value) {
            field = value
            scaleUtil.scrollMode = value
        }

    // TouchEvent
    private var downX = 0f
    private var downY = 0f
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var isMoving = false
    private var mAlwaysInTapRegion = true

    // 滑动惯性
    private val scroller by lazy { Scroller(context) }
    private var lastFlingX = 0
    private var lastFlingY = 0
    private val flingRunnable = Runnable {
        if (scroller.computeScrollOffset()) {
            val x = scroller.currX
            val y = scroller.currY
            val dx = x - lastFlingX
            val dy = y - lastFlingY
            val canMove = scaleUtil.onMove(dx.toFloat(), dy.toFloat())
            lastFlingX = x
            lastFlingY = y
            if (canMove) {
                postOnAnimationFun()
            }
        }
    }

    private val scaleUtil by lazy {
        ViewScaleUtil(minScale = minScale, maxScale = maxScale, scrollMode = scrollMode)
    }

    private var listener: View.OnClickListener? = null

    private val scaleGesture by lazy {
        ScaleGestureDetector(context, this)
    }

    private val mGestureDetector by lazy {
        GestureDetector(context, this)
    }

    init {
        view?.let {
            setTargetView(it)
        }
        listener?.let {
            setOnClickListener(it)
        }
    }

    fun setOnClickListener(listener: View.OnClickListener?) {
        this.listener = listener
    }

    fun setTargetView(view: View) {
        scaleUtil.view = view
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        if (enableScale) {
            scaleGesture.onTouchEvent(event)
        }
        if (enableMove) {
            mGestureDetector.onTouchEvent(event)
        }
        return checkTouchEventIsConsumed(event)
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        scaleUtil.onScale(detector.focusX, detector.focusY, detector.scaleFactor)
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        isMoving = scaleUtil.onMove(distanceX, distanceY)
        return false
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        lastFlingX = 0
        lastFlingY = 0
        scroller.fling(
            0,
            0,
            -velocityX.toInt(),
            -velocityY.toInt(),
            Int.MIN_VALUE,
            Int.MAX_VALUE,
            Int.MIN_VALUE,
            Int.MAX_VALUE
        )
        postOnAnimationFun()
        return super.onFling(e1, e2, velocityX, velocityY)
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        listener?.onClick(scaleUtil.view)
        return false
    }

    private fun checkTouchEventIsConsumed(event: MotionEvent): Boolean {
        return if (event.action == MotionEvent.ACTION_DOWN) {
            downX = event.x
            downY = event.y
            mAlwaysInTapRegion = true
            true
        } else {
            if (mAlwaysInTapRegion) {
                val dx = event.x - downX
                val dy = event.y - downY
                mAlwaysInTapRegion = (dx * dx) + (dy * dy) <= (touchSlop * touchSlop)
            }
            (enableScale && event.pointerCount > 1) || (enableMove && isMoving) || mAlwaysInTapRegion
        }
    }

    private fun postOnAnimationFun() {
        // 使Runnable在下一个动画时间步长上执行
        scaleUtil.view.postOnAnimation(flingRunnable)
    }
}

class ViewScaleUtil(
    targetView: View? = null,
    var minScale: Float = 1f,
    var maxScale: Float = 3f,
    // 0:VERTICAL 1:HORIZONTAL
    var scrollMode: Int = 0,
) : ScaleUtil() {

    lateinit var view: View

    override val width: Int get() = view.width
    override val height: Int get() = view.height

    val maxOffsetX get() = width * (view.scaleX - 1) / 2
    val maxOffsetY get() = height * (view.scaleY - 1) / 2
    val curOffsetX get() = view.translationX
    val curOffsetY get() = view.translationY

    init {
        targetView?.let {
            view = it
        }
    }

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
        val canMove = checkCanMove(dx, dy)
        if (canMove) {
            val newOffsetX = curOffsetX - dx
            val newOffsetY = curOffsetY - dy
            with(view) {
                translationX = newOffsetX.coerceIn(-maxOffsetX, maxOffsetX)
                translationY = newOffsetY.coerceIn(-maxOffsetY, maxOffsetY)
            }
        }
        return canMove
    }

    private fun checkCanMove(dx: Float, dy: Float): Boolean {
        val horizontalScroll = abs(dx) > abs(dy)
        val onLeftBound = curOffsetX == maxOffsetX
        val onRightBound = curOffsetX == -maxOffsetX
        val onTopBound = curOffsetY == maxOffsetY
        val onBottomBound = curOffsetY == -maxOffsetY
        return if (scrollMode == 1 && horizontalScroll) {
            !((onLeftBound && dx < 0) || (onRightBound && dx > 0))
        } else if (scrollMode == 0 && !horizontalScroll) {
            !((onTopBound && dy < 0) || (onBottomBound && dy > 0))
        } else {
            true
        }
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
        // 缩放中心偏移
        val diffX = scaleX - newScaleX
        val diffY = scaleY - newScaleY
        // 新的偏移
        val newOffsetX = offsetX + diffX
        val newOffsetY = offsetY + diffY
        result(newOffsetX, newOffsetY)
    }

}