package com.gfz.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.View
import com.gfz.bitmap.gifEncoder.AnimatedGifEncoder
import com.gfz.common.ext.getCompatDrawable
import com.gfz.common.utils.LocalFileUtil
import com.gfz.common.utils.TopLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

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
        withContext(Dispatchers.IO) {
            val output = FileOutputStream(filePath)
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, output)
            bmp.recycle()
            try {
                output.flush()
                output.close()
            } catch (e: Exception) {
                TopLog.e(e)
            }
            TopLog.e(filePath)
        }

    @JvmOverloads
    suspend fun bmpToByteArray(bmp: Bitmap, quality: Int = 100): ByteArray =
        withContext(Dispatchers.IO) {
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
            result
        }

    /**
     * 将view的内容绘制成Bitmap
     */
    suspend fun convertViewToBitmap(view: View): Bitmap? = withContext(Dispatchers.IO) {
        val w = view.measuredWidth
        val h = view.measuredHeight
        if (w <= 0 || h <= 0) {
            return@withContext null
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
        //利用bitmap生成画布
        val canvas = Canvas(bitmap)
        //把view中的内容绘制在画布上
        view.draw(canvas)
        bitmap
    }

    suspend fun createGif(
        picPath: String,
        gifPath: String,
        build: AnimatedGifEncoder.() -> Unit = {}
    ): Boolean =
        withContext(Dispatchers.IO) {
            val inputFile = File(picPath)
            if (!inputFile.exists() || !inputFile.isDirectory) {
                return@withContext false
            }
            createGif(gifPath, build) {
                inputFile.list()?.forEach {
                    val filePath = inputFile.absolutePath + File.separator + it
                    val bitmap = BitmapFactory.decodeFile(filePath)
                    addFrame(bitmap)
                }
            }

        }

    suspend fun createGif(
        bitmapArray: List<Bitmap>,
        gifPath: String,
        build: AnimatedGifEncoder.() -> Unit = {}
    ): Boolean =
        withContext(Dispatchers.IO) {
            if (bitmapArray.isEmpty()) {
                return@withContext false
            }
            createGif(gifPath, build) {
                bitmapArray.forEach {
                    addFrame(it)
                }
            }

        }

    suspend fun createGif(
        gifPath: String,
        build: AnimatedGifEncoder.() -> Unit = {},
        addFrames: AnimatedGifEncoder.() -> Unit
    ): Boolean =
        withContext(Dispatchers.IO) {
            val baos = ByteArrayOutputStream()
            build(gifEncoder)
            gifEncoder.start(baos)
            addFrames(gifEncoder)
            gifEncoder.finish()
            LocalFileUtil.writeFile(gifPath, baos)
        }
}