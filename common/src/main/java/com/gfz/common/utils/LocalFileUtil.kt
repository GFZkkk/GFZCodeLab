package com.gfz.common.utils

import android.R.attr.path
import android.content.Context
import android.os.Environment
import com.gfz.common.base.BaseApplication
import java.io.*


/**
 * 本地文件管理
 * created by gaofengze on 2021/4/30
 */
object LocalFileUtil {

    /**
     * 获取文件存储位置，优先外部存储
     */
    fun getFilePath(dir: String, context: Context = BaseApplication.appContext): String {
        val filePath = getExternalFilePath(dir, context) ?: getAppFilePath(dir, context)
        makeRootDirectory(filePath)
        return filePath.plus(File.separator)
    }

    /**
     * 获取文件存储位置，优先外部存储
     */
    fun getCacheFilePath(dir: String? = null, context: Context = BaseApplication.appContext): String {
        val path = getExternalCachePath(context) ?: getAppCachePath(context)
        var filePath = path
        dir?.let {
            filePath = filePath.plus(File.separator)
            filePath = filePath.plus(it)
        }
        makeRootDirectory(filePath)
        return filePath.plus(File.separator)
    }

    fun writeFile(filePath: String, inputStream: ByteArrayOutputStream): Boolean {
        try {
            val fos = FileOutputStream(filePath)
            inputStream.writeTo(fos)
            inputStream.flush()
            fos.flush()
            inputStream.close()
            fos.close()
        } catch (e: Exception) {
            TopLog.e(e)
            return false
        }
        return true
    }

    /**
     * 生成文件夹
     *
     * @param filePath
     */
    fun makeRootDirectory(filePath: String) {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                file.mkdirs()
            }
        } catch (e: Exception) {
            TopLog.e("生成文件夹失败：" + e.message)
        }
    }

    private fun getAppFilePath(dir: String, context: Context): String {
        return context.filesDir.toString().plus(File.separator).plus(dir)
    }

    private fun getExternalFilePath(dir: String, context: Context): String? {
        return if (haveExternalStorage()){
            context.getExternalFilesDir(dir)?.absolutePath
        } else{
            null
        }
    }

    private fun getAppCachePath(context: Context): String {
        return context.cacheDir.toString() + File.separator + "cache"
    }

    private fun getExternalCachePath(context: Context): String? {
        return if (haveExternalStorage()){
            context.externalCacheDir?.absolutePath
        } else{
            null
        }
    }

    private fun haveExternalStorage(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }
}