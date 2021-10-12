package com.gfz.common

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
        printLog(E, null, msg!!)
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
            val contents = wrapperContent(tagStr, msg)
            val tag = contents[0]
            val msg = contents[1]
            val headString = contents[2]
            printLog(type, tag, headString + msg)
        }
    }

    private fun wrapperContent(
        tagStr: String?,
        msg: Any
    ): Array<String> {
        val stackTrace =
            Thread.currentThread().stackTrace
        val index: Byte = 5
        val className = stackTrace[index.toInt()].fileName
        val methodName = stackTrace[index.toInt()].methodName
        val lineNumber = stackTrace[index.toInt()].lineNumber
        val methodNameShort =
            methodName.substring(0, 1).uppercase() + methodName.substring(1)
        val stringBuilder = StringBuilder()
        stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#")
            .append(methodNameShort).append(" ] ")
        val tag = tagStr ?: className
        val headString = stringBuilder.toString()
        return arrayOf(tag.toString(), msg.toString(), headString)
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