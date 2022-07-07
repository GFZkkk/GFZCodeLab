package com.gfz.lab.utils

import android.os.Handler
import android.os.Looper
import com.gfz.common.task.RecyclerPool
import com.gfz.common.task.TimeLoop
import com.gfz.common.utils.TopLog
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

/**
 * WebSocket工具
 * created by xueya on 2022/4/15
 *
 * val socket = WebSocketUtil.startWebSocketByUrl(WebSocketUtil.TEST_URL)
 * socket.addListener(this@TestCustomFragment)
 * socket.connect()
 */
object WebSocketUtil {
    const val TEST_URL = "ws://121.40.165.18:8800"
    private const val CONNECT_TIMEOUT = 3000L
    private const val READ_TIMEOUT = 3000L
    const val HEART_BEAT_RATE = 2 * 60 * 1000

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

class WebSocketCell(private val url: String) : WebSocketListener(), MessageCallback {

    var state: WebSocketState = WebSocketState.UN_CONNECT
    var isConnecting = false

    private val request: Request by lazy {
        Request.Builder().url(url).build()
    }

    private val heartBeatLoop: TimeLoop by lazy {
        TimeLoop.createTimerLoop(WebSocketUtil.HEART_BEAT_RATE) {
            if (curWebSocket?.send("") == false) {
                state = WebSocketState.OFFLINE
                connect()
            }
        }
    }
    var curWebSocket: WebSocket? = null

    private var listeners = HashSet<MessageCallback>()

    fun addListener(listener: MessageCallback) {
        listeners.add(listener)
    }

    fun removeListener(listener: MessageCallback) {
        listeners.remove(listener)
    }

    fun sendMessage(message: String): Boolean {
        return curWebSocket?.send(message) ?: false
    }

    fun connect() {
        if (isConnecting || state == WebSocketState.ONLINE) {
            return
        }
        try {
            isConnecting = true
            WebSocketUtil.build(request, this)
        } catch (e: Exception) {
            isConnecting = false
            TopLog.e(e)
        }
    }

    fun close() {
        try {
            heartBeatLoop.remove()
            curWebSocket?.cancel()
            curWebSocket?.close(1001, "关闭连接")
        }catch (e: Exception){
            TopLog.e(e)
        }
    }

    fun release(){
        close()
        listeners.clear()
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        TopLog.e("onOpen")
        curWebSocket = webSocket
        state = WebSocketState.ONLINE
        isConnecting = false
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        TopLog.e("onMessage:$text")
        curWebSocket = webSocket
        state = WebSocketState.ONLINE
        onMessage(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        TopLog.e("onMessage")
        curWebSocket = webSocket
        state = WebSocketState.ONLINE
        onMessage(bytes)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        TopLog.e("onClosing")
        curWebSocket = null
        heartBeatLoop.remove()
        state = WebSocketState.CLOSE
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        TopLog.e("onClosed")
        curWebSocket = null
        heartBeatLoop.remove()
        state = WebSocketState.UN_CONNECT
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        TopLog.e("onFailure:${t.message}")
        curWebSocket = webSocket
        state = WebSocketState.OFFLINE
        curWebSocket?.cancel()
        isConnecting = false
    }

    override fun onMessage(text: String) {
        val it = listeners.iterator()
        while (it.hasNext()) {
            it.next().onMessage(text)
        }
    }

    override fun onMessage(bytes: ByteString) {
        val it = listeners.iterator()
        while (it.hasNext()) {
            it.next().onMessage(bytes)
        }
    }
}

interface MessageCallback {
    fun onMessage(text: String)

    fun onMessage(bytes: ByteString) {

    }
}

enum class WebSocketState {
    UN_CONNECT,
    ONLINE,
    OFFLINE,
    CLOSE
}