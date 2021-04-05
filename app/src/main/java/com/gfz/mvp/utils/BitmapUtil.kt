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
        if (view == null) {
            return null
        }
        val w = view.measuredWidth
        val h = view.measuredHeight
        if (w <= 0 || h <= 0) {
            return null
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        //利用bitmap生成画布
        val canvas = Canvas(bitmap)
        //把view中的内容绘制在画布上
        view.draw(canvas)
        return bitmap
    }

    @JvmOverloads
    fun bmpToByteArray(bmp: Bitmap?, quality: Int = 100): ByteArray? {
        if (bmp == null) {
            return null
        }
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, output)
        bmp.recycle()
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
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
        var bitmap: Bitmap? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var vectorDrawable: Drawable? = null
            vectorDrawable = context.getDrawable(resId)
            bitmap = Bitmap.createBitmap(
                vectorDrawable!!.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
        } else {
            bitmap = BitmapFactory.decodeResource(context.resources, resId)
        }
        return bitmap
    }
}