package com.gfz.common

import android.annotation.SuppressLint
import android.content.Context
import com.gfz.common.utils.TopLog
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

/**
 *
 * created by gaofengze on 2021/5/13
 */
@SuppressLint("StaticFieldLeak")
object KTCatchException : Thread.UncaughtExceptionHandler {

    var context: Context? = null
    private var mDefaultException: Thread.UncaughtExceptionHandler? = null

    @SuppressLint("StaticFieldLeak")
    //获取系统默认的异常处理器,并且设置本类为系统默认处理器
    fun init(ctx: Context) {
        context = ctx
        // 获取系统默认的UncaughtException处理器
        mDefaultException = Thread.getDefaultUncaughtExceptionHandler()
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        mDefaultException?.uncaughtException(thread, ex)
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        TopLog.e("闪退：$writer")
    }
}