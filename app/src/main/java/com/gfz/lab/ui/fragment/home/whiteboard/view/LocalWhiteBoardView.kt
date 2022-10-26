package com.gfz.lab.ui.fragment.home.whiteboard.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gfz.common.utils.TopLog
import com.gfz.lab.ui.fragment.home.whiteboard.entity.PathEntity

/**
 * 本地画板
 * created by xueya on 2022/10/25
 */
class LocalWhiteBoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val isDebug = false

    private var scale = 1.0
    private val boardWidth = 375
    private val boardHeight = 555

    private val red = Color.parseColor("#FF3A3A")
    private val yellow = Color.parseColor("#FFE257")
    private val blue = Color.parseColor("#5AC6FF")

    private var isEraser = false
    private var currentColor = red
    private var strokeWidth = 10f
    private val eraserWidth = 100f
    private val eraserRadius = eraserWidth / 2

    private val pathList = mutableListOf<PathEntity>() // 保存涂鸦轨迹的集合
    private var lastX = 0f
    private var lastY = 0f
    private var currentPath: PathEntity? = null // 当前的涂鸦轨迹
    private var eraserPath: PathEntity? = null // 当前的橡皮轨迹
    private var downX = 0f
    private var downY = 0f

    private var mBufferBitmap: Bitmap? = null
    private var mBufferCanvas: Canvas? = null

    private val measure = PathMeasure()
    private val position = FloatArray(2)
    private val eraserRectF = RectF()

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }

    init {

    }

    fun setIsEraser(isEraser: Boolean) {
        this.isEraser = isEraser
        if (isDebug){
            if (isEraser) {
                if (eraserPath == null) {
                    eraserPath = PathEntity(color = blue, strokeWidth = eraserWidth)
                }
            } else {
                eraserPath = null
            }
        }
        invalidate()
    }

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

        if (mBufferCanvas == null) {
            mBufferBitmap =
                Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)

            //canvas绘制的内容，将会在这个mBufferBitmap内
            mBufferCanvas = Canvas(mBufferBitmap!!)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }

        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = x
                downY = y
                currentPath = PathEntity(color = currentColor, strokeWidth = strokeWidth)
                pathList.add(currentPath!!)
                currentPath?.path?.moveTo(x, y)
                currentPath?.range?.set(x, y, x, y)
                lastX = x
                lastY = y
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                if (isEraser) {
                    eraserLine(x, y)
                    if (isDebug){
                        eraserPath?.path?.addRect(RectF().apply {
                            set(x, y, x + 1, y + 1)
                        }, Path.Direction.CW)
                    }
                    invalidate()
                } else {
                    if (lastX != x || lastY != y) {
                        currentPath?.path?.quadTo(
                            lastX,
                            lastY,
                            (x + lastX) / 2,
                            (y + lastY) / 2
                        )
                        updatePathRange(x, y)
                        lastX = x
                        lastY = y
                        invalidate()
                    }
                }


            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mBufferCanvas?.drawColor(0, PorterDuff.Mode.CLEAR)
        mBufferCanvas?.drawColor(Color.parseColor("#33000000"))
        pathList.forEach {
            paint.color = it.color
            paint.strokeWidth = it.strokeWidth
            mBufferCanvas?.drawPath(it.path, paint)
            paint.color = yellow
            paint.strokeWidth = 5f
            if (isDebug){
                mBufferCanvas?.drawRect(it.range, paint)
            }
        }
        if (isDebug){
            eraserPath?.let {
                paint.color = it.color
                paint.strokeWidth = it.strokeWidth
                mBufferCanvas?.drawPath(it.path, paint)
            }
        }
        mBufferBitmap?.let {
            canvas?.drawBitmap(it, 0f, 0f, null)
        }
    }

    private fun eraserLine(x: Float, y: Float) {
        updateEraserRectF(x, y)
        val success = pathList.removeIf {
            if (RectF.intersects(it.range, eraserRectF)) {
                return@removeIf canEraserPath(it.path, eraserRectF)
            }
            return@removeIf false
        }
        if (success) {
            invalidate()
        }
    }

    private fun canEraserPath(path: Path, rectf: RectF): Boolean {
        eraserRectF.run {
            TopLog.e("eraserRectF:$left, $right, $top, $bottom")
        }
        measure.setPath(path, false)
        val step = eraserWidth
        var distance = 0f
        val length = measure.length

        while (distance < length) {
            if (measure.getPosTan(distance, position, null)) {
                TopLog.e("getPosTan:${position[0]}, ${position[1]}")
                if (rectf.contains(position[0], position[1])) {
                    TopLog.e("擦除")
                    return true
                }
            }
            distance = (distance + step).coerceAtMost(length)
        }
        return false
    }

    private fun updateEraserRectF(x: Float, y: Float) {
        val left = x - eraserRadius
        val right = left + eraserWidth
        val top = y - eraserRadius
        val bottom = top + eraserWidth
        eraserRectF.set(left, top, right, bottom)
    }

    private fun updatePathRange(x: Float, y: Float) {
        currentPath?.range?.let {
            if (x < it.left){
                it.left = x
            }else if(x > it.right){
                it.right = x
            }
            if (y < it.top){
                it.top = y
            } else if(y > it.bottom){
                it.bottom = y
            }
        }
    }
}