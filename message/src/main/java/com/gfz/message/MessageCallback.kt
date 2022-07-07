package com.gfz.message

import okio.ByteString

/**
 *
 * created by xueya on 2022/7/7
 */
interface MessageCallback {
    fun onMessage(text: String)

    fun onMessage(bytes: ByteString)
}