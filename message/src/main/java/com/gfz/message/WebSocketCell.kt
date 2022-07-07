package com.gfz.message

import com.gfz.common.task.TimeLoop
import com.gfz.common.utils.TopLog
import com.gfz.message.enum.WebSocketStateEnum
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

/**
 *
 * created by xueya on 2022/7/7
 */
class WebSocketCell(private val url: String) : WebSocketListener(), MessageCallback {

    var state: WebSocketStateEnum = WebSocketStateEnum.UN_CONNECT
    var isConnecting = false

    private val request: Request by lazy {
        Request.Builder().url(url).build()
    }

    private val heartBeatLoop: TimeLoop by lazy {
        TimeLoop.createTimerLoop(MessageManger.HEART_BEAT_RATE) {
            if (curWebSocket?.send("") == false) {
                state = WebSocketStateEnum.OFFLINE
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
        if (isConnecting || state == WebSocketStateEnum.ONLINE) {
            return
        }
        try {
            isConnecting = true
            MessageManger.build(request, this)
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
        state = WebSocketStateEnum.ONLINE
        isConnecting = false
        heartBeatLoop.startAfterDelay()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        TopLog.e("onMessage:$text")
        curWebSocket = webSocket
        state = WebSocketStateEnum.ONLINE
        onMessage(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        TopLog.e("onMessage")
        curWebSocket = webSocket
        state = WebSocketStateEnum.ONLINE
        onMessage(bytes)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        TopLog.e("onClosing")
        curWebSocket = null
        heartBeatLoop.remove()
        state = WebSocketStateEnum.CLOSE
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        TopLog.e("onClosed")
        curWebSocket = null
        heartBeatLoop.remove()
        state = WebSocketStateEnum.UN_CONNECT
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        TopLog.e("onFailure:${t.message}")
        curWebSocket = webSocket
        state = WebSocketStateEnum.OFFLINE
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