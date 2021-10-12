package com.gfz.common

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * 本地文件管理
 * created by gaofengze on 2021/4/30
 */
object LocalFileUtil {

    private fun getAppFilePath(context: Context): String {
        return context.filesDir.toString() + File.separator
    }

    /**
     * 获取文件存储位置，优先外部存储
     */
    fun getFilePath(context: Context, dir: String): String? {
        return (if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) { //判断外部存储是否可用
            context.getExternalFilesDir(dir)?.absolutePath
        } else { //没外部存储就使用内部存储
            getAppFilePath(context).plus(dir)
        })?.let {
            makeRootDirectory(it)
            it.plus(File.separator)
        }
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
}