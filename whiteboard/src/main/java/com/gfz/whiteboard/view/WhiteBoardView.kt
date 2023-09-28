package com.gfz.whiteboard.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.drawToBitmap
import com.gfz.whiteboard.databinding.ViewWhiteBoardBinding
import com.gfz.whiteboard.entity.PageEntity
import com.gfz.whiteboard.entity.PathEntity
import id.zelory.compressor.compressFormat
import id.zelory.compressor.saveBitmap
import java.io.File

/**
 *
 * created by xueya on 2022/10/26
 */
enum class LiveToolMode {
    None, Score, Paint, Eraser
}

interface WhiteBoard : WhiteBoardImage, WhiteBoardPaint {
    suspend fun saveBoardImage(path: String)
    fun destroy()
}

class WhiteBoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), WhiteBoard {

    private var url = ""

    private val binding = ViewWhiteBoardBinding.inflate(LayoutInflater.from(context), this, true)

    fun init(callBack: WhiteBoardPaintCallBack) {
        binding.whiteBoardPaint.setCallBack(callBack)
    }

    fun loadBoard(page: PageEntity) {
        setImage(page.background.url)
        initPointData(page.pageId, page.pathList)
    }

    override suspend fun saveBoardImage(path: String) {
        if (width == 0 || height == 0) {
            return
        }
        val bitmap = (this.parent as ViewGroup).drawToBitmap()
        val file = File(path)
        saveBitmap(bitmap, file, file.compressFormat(), 80)
    }

    override fun destroy() {
        binding.whiteBoardPaint.setCallBack(null)
    }

    override fun setImage(url: String) {
        this.url = url
        binding.whiteBoardImage.setImage(url)
    }

    override fun setToolType(type: LiveToolMode) {
        binding.whiteBoardPaint.setToolType(type)
    }

    override fun setStrokeColor(color: String) {
        binding.whiteBoardPaint.setStrokeColor(color)
    }

    override fun setStrokeWidth(width: Float) {
        binding.whiteBoardPaint.setStrokeWidth(width)
    }

    override fun moveBoard(dx: Float, dy: Float): Boolean {
        return binding.whiteBoardPaint.moveBoard(dx, dy)
    }

    override fun scaleBoard(scale: Float, px: Float, py: Float) {
        binding.whiteBoardPaint.scaleBoard(scale, px, py)
    }

    override fun initPointData(pageId: String, list: List<PathEntity>) {
        binding.whiteBoardPaint.initPointData(pageId, list)
    }

    override fun addPointData(pageId: String, entity: PathEntity) {
        binding.whiteBoardPaint.addPointData(pageId, entity)
    }

    override fun removePointData(idList: List<String>) {
        binding.whiteBoardPaint.removePointData(idList)
    }
}