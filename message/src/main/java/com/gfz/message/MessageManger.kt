package com.gfz.message

import com.gfz.common.utils.RecyclerPool
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

/**
 * WebSocket工具
 * created by xueya on 2022/4/15
 *
 * val socket = WebSocketUtil.startWebSocketByUrl(MessageManger.TEST_URL)
 * socket.addListener(this)
 * socket.connect()
 */
object MessageManger {

    private const val CONNECT_TIMEOUT = 3000L
    private const val READ_TIMEOUT = 3000L
    const val HEART_BEAT_RATE = 2 * 60 * 1000
    const val port = 8080

    const val TEST_URL = "ws://121.40.165.18:8800"

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()
    }

    private val WebSocketCellPool by lazy {
        RecyclerPool()
    }

    fun startWebSocketByUrl(url: String): WebSocketCell {
        return WebSocketCellPool.get(url.hashCode()) {
            WebSocketCell(url)
        }.apply {
            connect()
        }
    }

    fun closeWebSocketByUrl(url: String) {
        WebSocketCellPool.get<WebSocketCell>(url.hashCode())?.close()
    }

    fun build(request: Request, listener: WebSocketListener) {
        client.newWebSocket(request, listener)
    }
}