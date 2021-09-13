package com.gfz.lab.utils

import android.os.Environment
import com.gfz.lab.app.KTApp
import java.io.File

/**
 * 本地文件管理
 * created by gaofengze on 2021/4/30
 */
class LocalFileUtil {

    fun getAppFilePath(): String {
        return KTApp.appContext.filesDir.toString() + File.separator
    }

    /**
     * 获取文件存储位置，优先外部存储
     */
    fun getFilePath(dir: String): String? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) { //判断外部存储是否可用
            KTApp.appContext.getExternalFilesDir(dir)?.absolutePath
        } else { //没外部存储就使用内部存储
            getAppFilePath().plus(dir)
        }?.let {
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