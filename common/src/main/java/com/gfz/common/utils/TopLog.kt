package com.gfz.common.utils

import android.util.Log

object TopLog {
    const val V = 1
    const val D = 2
    const val I = 3
    const val W = 4
    const val E = 5

    private var IS_SHOW_LOG: Boolean = true

    fun init(isShowLog: Boolean) {
        IS_SHOW_LOG = isShowLog
    }

    fun i() {
        printLog(I, null, "execute")
    }

    fun i(msg: Any?) {
        printLog(I, null, msg!!)
    }

    //这个语法形式是什么意思？
    fun i(tag: String?, msg: Any) {
        printLog(I, tag, msg)
    }

    fun e() {
        printLog(E, null, "execute")
    }

    fun e(msg: Any?) {
        if (msg is Throwable){
            printError(E, null, msg)
        } else {
            printLog(E, null, msg!!)
        }
    }

    fun e(tag: String?, msg: Any) {
        printLog(E, tag, msg)
    }

    fun printLog(
        type: Int,
        tagStr: String?,
        msg: Any
    ) {
        if (IS_SHOW_LOG) {
            val contents = wrapperContent(msg)
            val tag = tagStr ?: "com.gfz.lab"
            printLog(type, tag, contents)
        }
    }

    fun printError(
        type: Int,
        tagStr: String?,
        error: Throwable
    ) {
        if (IS_SHOW_LOG) {
            val contents = Log.getStackTraceString(error)
            val tag = tagStr ?: "com.gfz.lab"
            printLog(type, tag, contents)
        }
    }

    private fun wrapperContent(msg: Any): String {
        val stackTrace =
            Thread.currentThread().stackTrace[5]
        val className = stackTrace.fileName
        val methodName = stackTrace.methodName
        val lineNumber = stackTrace.lineNumber
        return "[($className:$lineNumber)#$methodName] $msg"
    }

    /**
     * 打印日志
     * @param type
     * @param tag
     * @param sub
     */
    private fun printLog(type: Int, tag: String?, sub: String) {
        when (type) {
            V -> Log.v(tag, sub)
            D -> Log.d(tag, sub)
            I -> Log.i(tag, sub)
            W -> Log.w(tag, sub)
            E -> Log.e(tag, sub)
        }
    }
}