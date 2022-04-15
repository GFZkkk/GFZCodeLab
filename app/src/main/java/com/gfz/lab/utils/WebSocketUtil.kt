package com.gfz.lab.utils

import android.os.Handler
import android.os.Looper
import com.gfz.common.task.RecyclerPool
import com.gfz.common.task.TimeLoop
import com.gfz.common.utils.TopLog
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okio.ByteString
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 *
 * created by xueya on 2022/4/15
 */
object WebSocketUtil {
    const val TEST_URL = "ws://82.157.123.54:9010/ajaxchattest"
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

    fun getWebSocketByUrl(url: String): WebSocketCell{
        return WebSocketCellPool.get(url.hashCode()){
            WebSocketCell(url)
        }
    }

    fun closeWebSocketByUrl(url: String){
        WebSocketCellPool.get<WebSocketCell>(url.hashCode())?.close()
    }

    fun build(request: Request, listener: WebSocketListener) {
        client.newWebSocket(request, listener)
    }
}

class WebSocketCell(private val url: String) : WebSocketListener(), MessageCallback{

    var state: WebSocketState = WebSocketState.UN_CONNECT

    private val request: Request by lazy {
        Request.Builder().url(url).build()
    }

    var heartBeatLoop: TimeLoop? = null
    var curWebSocket: WebSocket? = null

    private var listeners = HashSet<MessageCallback>()

    fun addListener(listener: MessageCallback){
        listeners.add(listener)
    }

    fun removeListener(listener: MessageCallback){
        listeners.remove(listener)
    }

    suspend fun sendMessage(message: String):Boolean = suspendCancellableCoroutine {
        when(state){
            WebSocketState.UN_CONNECT ->
                it.resumeWithException(IllegalStateException("未建立连接"))
            WebSocketState.ONLINE ->
                it.resume(curWebSocket?.send(message)?:false)
            WebSocketState.OFFLINE ->
                it.resumeWithException(IllegalStateException("已离线"))
            WebSocketState.CLOSE ->
                it.resumeWithException(IllegalStateException("连接已关闭"))
        }
    }

    suspend fun connect(): WebSocketCell = suspendCancellableCoroutine{
        try {
            WebSocketUtil.build(request, this)
            it.resume(this)
        }catch (e: Exception){
            it.resumeWithException(e)
        }
    }

    fun close(){
        if (state == WebSocketState.ONLINE){
            curWebSocket?.cancel()
        }
        if (state != WebSocketState.CLOSE){
            curWebSocket?.close(1001,null)
        }
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        curWebSocket = webSocket
        heartBeatLoop = TimeLoop(Handler(Looper.getMainLooper()),
            WebSocketUtil.HEART_BEAT_RATE
        ){
            if(!webSocket.send("")){
                state = WebSocketState.OFFLINE
            }
        }
        state = WebSocketState.ONLINE
        TopLog.e("onOpen")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        curWebSocket = webSocket
        state = WebSocketState.ONLINE
        onMessage(text)
        TopLog.e("onMessage")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        curWebSocket = webSocket
        state = WebSocketState.ONLINE
        onMessage(bytes)
        TopLog.e("onMessage")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        curWebSocket = null
        heartBeatLoop?.remove()
        state = WebSocketState.CLOSE
        TopLog.e("onClosing")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        curWebSocket = null
        heartBeatLoop?.remove()
        state = WebSocketState.UN_CONNECT
        TopLog.e("onClosed")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        curWebSocket = webSocket
        state = WebSocketState.OFFLINE
        curWebSocket?.cancel()
        TopLog.e("onFailure:${t.message}")
    }

    override fun onMessage(text: String) {
        val it = listeners.iterator()
        while (it.hasNext()){
            it.next().onMessage(text)
        }

    }

    override fun onMessage(bytes: ByteString) {
        val it = listeners.iterator()
        while (it.hasNext()){
            it.next().onMessage(bytes)
        }
    }
}

interface MessageCallback{
    fun onMessage(text: String)

    fun onMessage(bytes: ByteString){

    }
}

enum class WebSocketState{
    UN_CONNECT,
    ONLINE,
    OFFLINE,
    CLOSE
}