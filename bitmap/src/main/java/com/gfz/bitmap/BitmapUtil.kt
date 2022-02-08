package com.gfz.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
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

    /**
     * 将view的内容绘制成bit数组
     */
    suspend fun convertViewToByteArray(view: View?, quality: Int): ByteArray? {
        return convertViewToBitmap(view)?.let {
            bmpToByteArray(it, quality)
        }
    }

    /**
     * 将view的内容绘制成bit数组
     */
    suspend fun convertViewToFile(fileName: String, view: View?, quality: Int = 100) {
        convertViewToBitmap(view)?.let {
            saveBitmapToFile(fileName, it, quality)
        }

    }

    suspend fun saveBitmapToFile(fileName: String, bmp: Bitmap, quality: Int = 100) =
        withContext(Dispatchers.IO) {
            val path = LocalFileUtil.getFilePath("images")
            val file = File(path, fileName.plus(".jpg"))
            val output = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, output)
            bmp.recycle()
            try {
                output.flush()
                output.close()
            } catch (e: Exception) {
                TopLog.e(e)
            }
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
    fun convertViewToBitmap(view: View?): Bitmap? {
        return view?.let {
            val w = it.measuredWidth
            val h = it.measuredHeight
            if (w <= 0 || h <= 0) {
                return null
            }
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            //利用bitmap生成画布
            val canvas = Canvas(bitmap)
            //把view中的内容绘制在画布上
            it.draw(canvas)
            bitmap
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
}