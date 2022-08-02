package com.gfz.gif

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.gfz.common.utils.LocalFileUtil
import com.gfz.gif.gifEncoder.AnimatedGifEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.coroutines.resume

/**
 *
 * created by xueya on 2022/3/18
 */
object GifUtil {
    private val gifEncoder: AnimatedGifEncoder by lazy {
        AnimatedGifEncoder().apply {
            setRepeat(0)
            setDelay(1000)
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
    ): Boolean {
        return try {
            withContext(Dispatchers.IO){
                val baos = ByteArrayOutputStream()
                build(gifEncoder)
                gifEncoder.start(baos)
                addFrames(gifEncoder)
                gifEncoder.finish()
                LocalFileUtil.writeFile(gifPath, baos)
            }
            true
        }catch (e: Exception){
            false
        }

    }
}