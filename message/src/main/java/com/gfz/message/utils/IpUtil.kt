package com.gfz.message.utils

import java.net.InetAddress

/**
 *
 * created by xueya on 2022/4/11
 */
object IpUtil {
    fun getIpAddress(): String{
        return InetAddress.getLocalHost().hostAddress?:""
    }
}