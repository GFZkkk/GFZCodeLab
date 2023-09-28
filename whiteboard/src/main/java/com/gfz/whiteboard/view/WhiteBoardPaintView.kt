package com.gfz.whiteboard.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gfz.common.ext.remove
import com.gfz.whiteboard.entity.PathEntity
import java.util.*
import kotlin.random.Random
import kotlin.random.nextLong

/**
 *
 * created by xueya on 2022/10/26
 */

interface WhiteBoardPaint : WhiteBoardPaintSync {
    fun setToolType(type: LiveToolMode)
    fun setStrokeColor(color: String)
    fun setStrokeWidth(width: Float)
    fun moveBoard(dx: Float, dy: Float): Boolean
    fun scaleBoard(scale: Float, px: Float, py: Float)
}

interface WhiteBoardPaintCallBack {
    fun onUploadPointData(pageId: String, pathEntity: PathEntity)
    fun onUploadEraserData(pageId: String, idList: List<String>)
}

interface WhiteBoardPaintSync {
    fun initPointData(pageId: String, list: List<PathEntity>)
    fun addPointData(pageId: String, entity: PathEntity)
    fun removePointData(idList: List<String>)
}

class WhiteBoardPaintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), WhiteBoardPaint, WhiteBoardPaintSync {

    // 画板比例
    private val ratio = 1.6

    // 画板越大，点的精度越高
    private val boardWidth = 1000
    private val boardHeight = (boardWidth * ratio).toInt()
    private val STROKE_WIDTH = boardWidth / 200f
    private val ERASER_WIDTH = STROKE_WIDTH * 15

    private var boardScale: Float? = null

    private var isEraser = false
    private var enable = false
    private var currentColor = "#000000"
    private var strokeScale = 1f

    private var eraserWidth = 0f
    private var eraserRadius = 0f

    private var pageId: String = ""
    private val pathList = mutableListOf<PathEntity>() // 保存涂鸦轨迹的集合

    private var lastX = 0f
    private var lastY = 0f
    private var currentPath: PathEntity? = null // 当前的涂鸦轨迹
    private var onceEraserPathIdList: MutableList<String> = mutableListOf()
    private var eraserPathIdSet: MutableSet<String> = mutableSetOf()

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

    private var callBack: WhiteBoardPaintCallBack? = null
    private var timeKey: String = ""

    init {
        val nowTime = System.currentTimeMillis()
        if (nowTime > 10000){
            timeKey = nowTime.toString().let {
                it.substring(it.length - 3)
            }
        }
    }
    // region 功能
    // 设置白板工具
    override fun setToolType(type: LiveToolMode) {
        when (type) {
            LiveToolMode.Paint, LiveToolMode.Eraser -> {
                enable = true
                isEraser = type == LiveToolMode.Eraser
            }
            else -> enable = false
        }
    }

    // 设置画笔颜色
    override fun setStrokeColor(color: String) {
        currentColor = color
    }

    // 设置画笔宽度
    override fun setStrokeWidth(width: Float) {
        strokeScale = width
    }

    override fun scaleBoard(scale: Float, px: Float, py: Float) {
        withCanvas {
            scale(scale, scale, px, py)
        }
    }

    override fun moveBoard(dx: Float, dy: Float): Boolean {
        withCanvas {
            translate(dx, dy)
        }
        return false
    }
    // endregion

    // region 初始化
    fun setCallBack(callBack: WhiteBoardPaintCallBack?) {
        this.callBack = callBack
    }

    private fun onInitScale(scale: Float) {
        if (boardScale == scale){
            return
        }
        boardScale = scale
        eraserWidth = ERASER_WIDTH * scale
        eraserRadius = eraserWidth / 2
        post {
            var change = false
            pathList.forEach {
                change = it.drawByData(scale) || change
            }
            if (change){
                drawBoard()
            }
        }
    }
    // endregion

    // region 同步
    override fun initPointData(pageId: String, list: List<PathEntity>) {
        clear()
        this.pageId = pageId
        // 如果当前已经获取到缩放，马上加载数据
        boardScale?.let { scale ->
            list.forEach {
                it.drawByData(scale)
            }
        }
        // 新增所有线
        pathList.addAll(list)
        // 绘制
        drawBoard()
    }

    private fun clear() {
        this.pageId = ""
        // 清空当前正在画的线
        currentPath = null
        // 清空所有线
        pathList.clear()
    }

    override fun addPointData(pageId: String, entity: PathEntity) {
        if (pageId != this.pageId) {
            return
        }
        boardScale?.let {
            entity.drawByData(it)
        }
        // 添加到正在画的线段之前
        if (currentPath != null) {
            pathList.add(pathList.size - 1, entity)
        } else {
            pathList.add(entity)
        }
        drawBoard()
    }

    override fun removePointData(idList: List<String>) {
        eraserPathIdSet.addAll(idList)
        if (pathList.remove { return@remove eraserPathIdSet.contains(id) }) {
            drawBoard()
        }
    }

    private fun uploadPointData() {
        currentPath?.let { it ->
            callBack?.onUploadPointData(pageId, it)
        }
        currentPath = null
    }

    private fun uploadEraserData() {
        if (onceEraserPathIdList.isEmpty()){
            return
        }
        val idList = onceEraserPathIdList.toList()
        onceEraserPathIdList.clear()
        callBack?.onUploadEraserData(pageId, idList)
    }
    // endregion

    // region 画线
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null || !enable) {
            return super.onTouchEvent(event)
        }
        val scale = boardScale ?: return super.onTouchEvent(event)
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isEraser) {
                    lastX = x
                    lastY = y
                    createPath()
                    // 移动到起始点
                    currentPath?.pathStart(x, y)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isEraser) {
                    eraserLine(x, y)
                } else {
                    currentPath?.drawByTouch(x, y, lastX, lastY, scale)
                    drawBoard()
                    lastX = x
                    lastY = y
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!isEraser) {
                    currentPath?.pathEnd()
                    uploadPointData()
                } else {
                    uploadEraserData()
                }
            }
        }
        return true
    }

    private fun createPath() {
        currentPath = PathEntity(
            id = getPathId(),
            color = currentColor,
            strokeWidth = (strokeScale * STROKE_WIDTH).toInt()
        ).apply {
            pathList.add(this)
        }
    }
    // endregion

    // region 擦除
    private fun eraserLine(x: Float, y: Float) {
        updateEraserRectF(x, y)
        val success = pathList.remove {
            if (RectF.intersects(bounds, eraserRectF)) {
                val needRemove = canEraserPath(path, eraserRectF)
                if (needRemove) {
                    eraserPathIdSet.add(id)
                    onceEraserPathIdList.add(id)
                }
                return@remove needRemove
            }
            return@remove false
        }
        if (success) {
            drawBoard()
        }
    }

    private fun canEraserPath(path: Path, rectf: RectF): Boolean {
        measure.setPath(path, false)
        // 生成点的间隔为橡皮的半径
        val step = eraserWidth / 2f
        // 画笔路径长度
        val length = measure.length
        // 当前位置
        var distance = 0f

        while (distance < length) {
            if (measure.getPosTan(distance, position, null)) {
                // 判断点是否在橡皮范围内，可优化为判断两点间距离
                if (rectf.contains(position[0], position[1])) {
                    return true
                }
            }
            // 下一个点的位置
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
    // endregion

    // region 工具方法
    private fun getPathId(): String {
        return buildString {
            // 创建key
            append(timeKey)
            // 当前时间戳
            append(System.currentTimeMillis())
            // 随机数
            append(Random.nextLong(LongRange(100,999)))
        }
    }
    // endregion

    // region 绘制
    private fun drawBoard() {
        withCanvas { scale ->
            save()
            // 初始化白板状态
            scale(1f, 1f)
            translate(0f, 0f)
            // 删除已有内容
            drawColor(0, PorterDuff.Mode.CLEAR)
            // 重新绘制所有路径
            pathList.forEach {
                paint.color = it.colorInt
                paint.strokeWidth = it.strokeWidth * scale
                drawPath(it.path, paint)
            }
            restore()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mBufferBitmap?.let {
            canvas?.drawBitmap(it, 0f, 0f, null)
        }
    }

    private fun withCanvas(block: Canvas.(Float) -> Unit) {
        mBufferCanvas?.let { canvas ->
            boardScale?.let { scale ->
                block(canvas, scale)
                invalidate()
            }
        }
    }
    // endregion

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)

        val wScale = 1.0 * w / boardWidth
        val hScale = 1.0 * h / boardHeight
        val scale = wScale.coerceAtMost(hScale).toFloat()
        onInitScale(scale)
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

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(enable)
        return super.dispatchTouchEvent(event)
    }
}