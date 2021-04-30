package com.gfz.mvp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import java.io.ByteArrayOutputStream

/**
 * created by gfz on 2021/2/15
 **/
object BitmapUtil {

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

    @JvmOverloads
    fun bmpToByteArray(bmp: Bitmap?, quality: Int = 100): ByteArray? {

        return bmp?.let {
            val output = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, output)
            bmp.recycle()
            val result = output.toByteArray()
            try {
                output.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            result
        }
    }

    /**
     * 将view的内容绘制成bit数组
     */
    fun convertViewToByteArray(view: View?, quality: Int): ByteArray? {
        return bmpToByteArray(convertViewToBitmap(view), quality)
    }

    /**
     * 获取矢量图
     */
    fun getVectorBitmap(context: Context, resId: Int): Bitmap? {
        // 矢量图需要在21之后的版本处理
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getCompatDrawable(resId)?.let {
                val bitmap = Bitmap.createBitmap(
                    it.intrinsicWidth,
                    it.intrinsicHeight, Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                it.setBounds(0, 0, canvas.width, canvas.height)
                it.draw(canvas)
                bitmap
            }
        } else {
            BitmapFactory.decodeResource(context.resources, resId)
        }
    }
}