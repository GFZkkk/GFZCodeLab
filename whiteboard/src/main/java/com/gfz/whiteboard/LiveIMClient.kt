package com.gfz.whiteboard

import android.net.Network
import androidx.lifecycle.lifecycleScope
import com.gfz.common.ext.launchSafe
import com.gfz.common.utils.MoshiUtil
import com.gfz.common.utils.TopLog
import com.gfz.whiteboard.entity.BoardListDataEntity
import com.xihang.module.partnertraining.live.customboard.entity.*
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.concurrent.timer
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.random.Random
import kotlin.random.nextLong

/**
 *
 * created by xueya on 2023/2/23
 */
class LiveIMClient private constructor(
//    private val config: CBoardEntity,
    private val roomOwner: LiveRoomOwner,
    private var clientCallback: LiveIMClientCallBack?
)  {

    companion object {
        const val LOGIN_CONNECTING = 2
        const val LOGIN_CONNECTED = 1
        const val LOGIN_FAILED = 0

        var liveIMClient: LiveIMClient? = null

        fun getInstance(
//            config: CBoardEntity,
            roomOwner: LiveRoomOwner,
            clientCallback: LiveIMClientCallBack
        ): LiveIMClient {
//            if (liveIMClient != null) {
//                if (liveIMClient?.config == config) {
//                    return liveIMClient!!
//                }
//                destroy()
//            }
//            liveIMClient = LiveIMClient(config, roomOwner, clientCallback)
            return liveIMClient!!
        }

        fun destroy() {
//            liveIMClient?.doDestroy()
            liveIMClient = null
        }
    }

    private var checkIMTimer: Timer? = null
    private var loginCancellable: CancellableContinuation<Int>? = null
    var imState = LOGIN_CONNECTING
        private set

    private val sendJobMap: MutableMap<LiveIMType, LinkedHashMap<String, Job>> = mutableMapOf()
    private var getDataContinuation: CancellableContinuation<BoardListDataEntity>? = null

    private var timeKey = ""
    private var boardCallback: LiveIMBoardCallBack? = null

    // 生成最新的快照的服务端id，所有比该id小的事件都不需要接收
    private var lastAllDataServerId = 0L

    // 获取最新乐谱时的服务端id，所有比该id小的刷新白板和翻页事件都不需要接受
    private var lastBoardDataServerId = 0L

    init {
        // 创建时间id
        timeKey = System.currentTimeMillis().toString().let {
            it.substring((it.length - 3).coerceAtLeast(0))
        }
    }

//    suspend fun login(): Int = suspendCancellableCoroutine { cancellable ->
//        // 中断上一次操作
//        if (loginCancellable != null) {
//            loginCancellable?.resumeWithException(ManagerError(ManagerError.INTERRUPT))
//            loginCancellable = null
//        }
//        // 等待连接
//        if (imState == LOGIN_CONNECTED) {
//            cancellable.resume(imState)
//        } else {
//            loginCancellable = cancellable
//        }
//        createIMTimer()
//    }
//
//    fun setCallback(callback: LiveIMBoardCallBack?) {
//        this.boardCallback = callback
//    }
//
//    suspend fun getAllBoardData(
//        syncLine: Boolean,
//        retryNum: Int = 3,
//        delayTime: Long = 1000
//    ): BoardListDataEntity =
//        suspendCancellableCoroutine {
//            cancelPullData()
//            getDataContinuation = it
//            roomOwner.lifecycleScope.launchSafe {
//                withRetry({
//                    TopLog.i("自研白板 获取白板信息")
//                    val entity = Network.get<BoardListDataEntity>(
//                        "$IMHost/whiteboard/synchronize/room",
//                        mapOf<String, Any>(
//                            "whiteRoomId" to config.whiteboardRoomId,
//                            "businessId" to config.businessId,
//                            "synchronizeLineData" to syncLine,
//                        )
//                    )
//                    if (syncLine) {
//                        lastAllDataServerId = entity.maxServerOperationLogId
//                    }
//                    lastBoardDataServerId = entity.maxServerOperationLogId
//                    it.resume(entity)
//                }, { _, times ->
//                    val needRetry = retryNum == -1 || times < retryNum
//                    if (needRetry) {
//                        delay(delayTime)
//                    }
//                    return@withRetry needRetry
//                })
//            }
//        }
//
//    fun sendMessageEvent(msg: String) {
//        buildEventAndSend(LiveIMType.Custom, msg)
//    }
//
//    fun buildEventAndSend(type: LiveIMType, data: String = "") {
//        val event = buildEvent().also {
//            it.type = type
//            it.data = data
//        }
//        sendEvent(event)
//    }
//
//    private fun sendEvent(event: LiveIMEventEntity, retryNum: Int = 0) {
//        val type = event.type
//        // 取消之前等待发送的请求
//        if (event.type.onlyResult) {
//            sendJobMap[type]?.apply {
//                keys.lastOrNull()?.let {
//                    remove(it)?.cancel()
//                }
//            }
//        }
//        // 发送
//        val job = buildJob(event, retryNum)
//        // 保存
//        val jobMap = sendJobMap[type] ?: linkedMapOf<String, Job>().apply {
//            sendJobMap[type] = this
//        }
//        jobMap[event.clientOperationId] = job
//    }
//
//    private fun buildJob(event: LiveIMEventEntity, retryNum: Int = 0): Job {
//        return roomOwner.lifecycleScope.launchSafe(onError = {
//            // 发送结束
//            sendJobMap[event.type]?.remove(event.clientOperationId)
//            // 重试一次
//            if (retryNum < 2) {
//                sendEvent(event, retryNum + 1)
//            } else {
//                TopLog.e("自研白板发送消息失败：retryNum:${retryNum} msg:${it.message}")
//            }
//        }) {
//            // 发送前等待
//            if (event.type.onlyResult) {
//                delay(500)
//            } else if (retryNum > 0) {
//                delay(1000)
//            }
//            // 发送事件
//            TopLog.i("自研白板：发送IM消息：${event.toMap()}")
//            // todo
////            Network.post<Any>("$IMHost/whiteboard/send/operation", event.toMap())
//            // 发送结束
//            sendJobMap[event.type]?.remove(event.clientOperationId)
//        }
//    }
//
//    private fun buildEvent(): LiveIMEventEntity {
//        return LiveIMEventEntity(
//            clientOperationId = getOperationId(),
//            timestamp = System.currentTimeMillis(),
//            whiteboardRoomId = config.whiteboardRoomId
//        )
//    }
//
//    // 生成唯一操作id
//    private fun getOperationId(): String {
//        return buildString {
//            append(config.whiteboardRoomId)
//            append(timeKey)
//            append(System.currentTimeMillis())
//            append(Random.nextLong(LongRange(100, 999)))
//        }
//    }
//
//    override fun onReceivedMessage(
//        clientOperationId: String,
//        whiteboardRoomId: String,
//        serverOperationLogId: Long,
//        type: Long,
//        data: String
//    ) {
//        if (serverOperationLogId <= lastAllDataServerId) {
//            return
//        }
//        when (LiveIMType.fromInt(type.toInt())) {
//            LiveIMType.AddLine -> {
//                MoshiUtil.fromJson<AddLineEvent>(data)?.let {
//                    boardCallback?.onAddLine(serverOperationLogId, it)
//                }
//            }
//            LiveIMType.RemoveLine -> {
//                MoshiUtil.fromJson<DeleteLinesEvent>(data)?.let {
//                    boardCallback?.onDeleteLine(serverOperationLogId, it)
//                }
//            }
//            LiveIMType.GotoPage -> {
//                if (serverOperationLogId > lastBoardDataServerId) {
//                    MoshiUtil.fromJson<GotoPageEvent>(data)?.let {
//                        boardCallback?.onPageIndexChanged(serverOperationLogId, it)
//                    }
//                }
//            }
//            LiveIMType.RefreshPage -> {
//                if (serverOperationLogId > lastBoardDataServerId) {
//                    boardCallback?.onUpdateBoard(serverOperationLogId)
//                }
//            }
//            LiveIMType.Custom -> {
//                clientCallback?.onMessageReceived(data)
//            }
//            LiveIMType.Unknown -> {}
//        }
//    }
//
//    override fun onConnectStatusChange(connected: Boolean) {
//        imState = if (connected) {
//            LOGIN_CONNECTED
//        } else {
//            LOGIN_CONNECTING
//        }
//
//        clientCallback?.onConnectionStateChanged(imState)
//        // 等待连接
//        if (loginCancellable != null && imState == LOGIN_CONNECTED) {
//            loginCancellable?.resume(imState)
//            loginCancellable = null
//        }
//    }
//
//    private fun cancelPullData() {
//        if (getDataContinuation != null && getDataContinuation!!.isActive) {
//            getDataContinuation?.resumeWithException(ManagerError(ManagerError.INTERRUPT))
//            getDataContinuation = null
//        }
//    }
//
//    private fun cancelSendEvent() {
//        sendJobMap.values.forEach {
//            it.values.forEach { job ->
//                if (job.isActive) {
//                    job.cancel()
//                }
//            }
//        }
//        sendJobMap.clear()
//    }
//
//    private fun cancelRequest() {
//        // 取消拉取乐谱数据
//        cancelPullData()
//        // 取消事件
//        cancelSendEvent()
//    }
//
//    private fun doDestroy() {
//        cancelRequest()
//
//        checkIMTimer?.cancel()
//        checkIMTimer = null
//
//        clientCallback = null
//    }
//
//    private fun createIMTimer() {
//        if (checkIMTimer == null) {
//            checkIMTimer = timer(initialDelay = 0, period = 5000) {
//                // 该方法需要在主线程调用
//                roomOwner.runOnUIThread {
//                    WebSocketFlutterHost.get().checkConnected(true) {}
//                }
//            }
//        }
//    }
//
//    private inline fun withRetry(
//        run: () -> Unit,
//        onRetry: (code: Int, times: Int) -> Boolean = { _, _ -> false },
//        onResult: (code: Int) -> Unit = { code -> ManagerError.checkManagerError(code) }
//    ) {
//        var code: Int? = null
//        var times = 0
//        while (code == null || (code != ManagerError.SUCCESS && onRetry(code, times++))) {
//            code = try {
//                run()
//                ManagerError.SUCCESS
//            } catch (e: Exception) {
//                ManagerError.ERROR
//            }
//        }
//        if (code != ManagerError.SUCCESS) {
//            onResult(code)
//        }
//    }

}

interface LiveIMBoardCallBack {
    fun onUpdateBoard(serverOperationLogId: Long)
    fun onAddLine(serverOperationLogId: Long, event: AddLineEvent)
    fun onDeleteLine(serverOperationLogId: Long, event: DeleteLinesEvent)
    fun onPageIndexChanged(serverOperationLogId: Long, event: GotoPageEvent)
}

interface LiveIMClientCallBack {
    fun onConnectionStateChanged(state: Int)
    fun onMessageReceived(text: String)
}

/**
 * @param onlyResult true：只关心结果 状态类型数据；false：关心过程 记录类型数据
 */
enum class LiveIMType(val type: Int, val onlyResult: Boolean) {
    AddLine(1, false),
    RemoveLine(2, false),
    GotoPage(3, true),
    RefreshPage(6, true),
    Custom(100, true),
    Unknown(-1, true);

    companion object {
        fun fromInt(type: Int): LiveIMType {
            values().forEach {
                if (it.type == type) {
                    return it
                }
            }
            return Unknown
        }
    }
}