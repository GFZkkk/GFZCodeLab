package com.gfz.bitmap

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.core.graphics.applyCanvas
import androidx.core.view.drawToBitmap
import com.gfz.bitmap.gifEncoder.AnimatedGifEncoder
import com.gfz.common.ext.getCompatDrawable
import com.gfz.common.utils.LocalFileUtil
import com.gfz.common.utils.TopLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume

/**
 * 图片处理工具类
 * created by gfz on 2021/2/15
 **/
object BitmapUtil {

    private val gifEncoder: AnimatedGifEncoder by lazy {
        AnimatedGifEncoder().apply {
            setRepeat(0)
            setDelay(1000)
        }
    }

    /**
     * 获取矢量图
     */
    fun getVectorBitmap(context: Context, resId: Int): Bitmap? {
        // 矢量图需要在21之后的版本处理
        return context.getCompatDrawable(resId)?.let {
            val bitmap = Bitmap.createBitmap(
                it.intrinsicWidth,
                it.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            it.setBounds(0, 0, canvas.width, canvas.height)
            it.draw(canvas)
            bitmap
        }
    }

    /**
     * 将view的内容绘制成bit数组
     */
    suspend fun convertViewToByteArray(view: View, quality: Int): ByteArray? {
        val bitmap = convertViewToBitmap(view)
        return bitmap?.let {
            bmpToByteArray(it, quality)
        }
    }

    /**
     * 将view的内容绘制成bit数组
     */
    suspend fun convertViewToFile(fileName: String, view: View, quality: Int = 100) {
        val bitmap = convertViewToBitmap(view)
        bitmap?.let {
            saveBitmapToFile(fileName, it, quality)
        }
    }

    suspend fun saveBitmapToFile(filePath: String, bmp: Bitmap, quality: Int = 100) =
        suspendCancellableCoroutine<Boolean> {
            val output = FileOutputStream(filePath)
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, output)
            bmp.recycle()
            try {
                output.flush()
                output.close()
            } catch (e: Exception) {
                TopLog.e(e)
            }
            it.resume(true)
        }

    @JvmOverloads
    suspend fun bmpToByteArray(bmp: Bitmap, quality: Int = 100): ByteArray =
        suspendCancellableCoroutine {
            val output = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, output)
            bmp.recycle()
            val result = output.toByteArray()
            try {
                output.flush()
                output.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            it.resume(result)
        }

    /**
     * 将view的内容绘制成Bitmap
     */
    suspend fun convertViewToBitmap(
        view: View,
        w: Int = 0,
        h: Int = 0,
        isCenter: Boolean = true,
        backgroundColor: Int = Color.WHITE,
        config: Bitmap.Config = Bitmap.Config.RGB_565
    ): Bitmap? =
        suspendCancellableCoroutine {
            val viewW = view.measuredWidth
            val viewH = view.measuredHeight
            if (viewW <= 0 || viewH <= 0) {
                it.resume(null)
                return@suspendCancellableCoroutine
            }
            if (w == 0 || h == 0) {
                it.resume(view.drawToBitmap(config))
            } else {
                val scale = if (1f * viewW / viewH > 1f * w / h){
                    1f * w / viewW
                } else {
                    1f * h / viewH
                }
                val x = if(isCenter) (w - (viewW * scale)) / 2 else 0f
                val y = if(isCenter) (h - (viewH * scale)) / 2 else 0f
                val paint = Paint(Paint.FILTER_BITMAP_FLAG)
                val bitmap = Bitmap.createBitmap(w, h, config).applyCanvas {
                    drawColor(backgroundColor)
                    translate(x, y)
                    drawBitmap(view.drawToBitmap(config), Matrix().apply {
                        setScale(scale, scale)
                    }, paint)
                }
                it.resume(bitmap)
            }
        }

    suspend fun createGif(
        picPath: String,
        gifPath: String,
        build: AnimatedGifEncoder.() -> Unit = {}
    ): Boolean {
        val inputFile = File(picPath)
        if (!inputFile.exists() || !inputFile.isDirectory) {
            return false
        }
        val result = inputFile.list()?.let { files ->
            val bitmapList = ArrayList<Bitmap>(files.size)
            files.forEach {
                val filePath = inputFile.absolutePath + File.separator + it
                val bitmap = BitmapFactory.decodeFile(filePath)
                bitmapList.add(bitmap)
            }
            createGif(bitmapList, gifPath, build)
        } ?: false

        return result
    }

    suspend fun createGif(
        bitmapArray: List<Bitmap>,
        gifPath: String,
        build: AnimatedGifEncoder.() -> Unit = {}
    ): Boolean {
        if (bitmapArray.isEmpty()) {
            return false
        }
        var width = 0
        var height = 0
        bitmapArray.forEach {
            width = it.width.coerceAtLeast(width)
            height = it.height.coerceAtLeast(height)
        }

        val result = createGif(gifPath, {
            build()
            setSize(width, height)
        }) {
            bitmapArray.forEach {
                addFrame(it)
            }
        }
        return result
    }

    private suspend fun createGif(
        gifPath: String,
        build: AnimatedGifEncoder.() -> Unit = {},
        addFrames: AnimatedGifEncoder.() -> Unit
    ): Boolean = suspendCancellableCoroutine {
        try {
            val baos = ByteArrayOutputStream()
            build(gifEncoder)
            gifEncoder.start(baos)
            addFrames(gifEncoder)
            gifEncoder.finish()
            LocalFileUtil.writeFile(gifPath, baos)
            it.resume(true)
        } catch (e: Exception) {
            it.resume(false)
        }

    }
}