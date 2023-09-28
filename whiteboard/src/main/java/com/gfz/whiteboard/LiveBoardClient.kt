package com.gfz.whiteboard

import androidx.lifecycle.lifecycleScope
import com.gfz.common.ext.launchSafe
import com.gfz.common.utils.MoshiUtil
import com.gfz.common.utils.TopLog
import com.gfz.whiteboard.entity.BoardDataEntity
import com.xihang.module.partnertraining.live.*
import com.xihang.module.partnertraining.live.customboard.entity.*
import com.gfz.whiteboard.view.WhiteBoardPaintCallBack
import com.gfz.whiteboard.view.WhiteBoardView
import com.gfz.whiteboard.entity.LiveBoardEntity
import com.gfz.whiteboard.entity.PathEntity
import kotlinx.coroutines.CoroutineScope

/**
 *
 * created by xueya on 2023/2/23
 */

class LiveBoardClient(
    private val whiteBoardView: WhiteBoardView,
    private val roomOwner: LiveRoomOwner,
    private val callback: LiveBoardClientCallBack? = null
) : LiveIMBoardCallBack, WhiteBoardPaintCallBack {

    private val dataEntity = LiveBoardEntity()

    // 在拉取数据过程中，收到的暂时无法处理的数据
    // 未处理的翻页事件
    private var pageServerOperationId = 0L
    private var lastUnhandledPageId = ""
    // 未处理的更新乐谱事件
    private var boardServerOperationId = 0L

    init {
        withImClient {
            setCallback(this@LiveBoardClient)
        }
        whiteBoardView.init(this)
    }

    suspend fun init() {
        // 同步数据
        syncAllData()
        // 加载白板
        loadBoard()
    }

    fun pause(){
        dataEntity.clear()
    }

    fun destroy() {
        whiteBoardView.destroy()
        withImClient {
            setCallback(null)
        }
    }

    fun setBoardList(pageList: List<BoardDataEntity>) {
        dataEntity.setPage(pageList)
    }

    fun getCurPageIndex(): Int = dataEntity.getCurPageIndex()

    // region 发送
    fun gotoBoard(index: Int) {
        if (!dataEntity.init){
            return
        }
        TopLog.i("自研白板-翻页")
        val pageId = dataEntity.getPageId(index)
        if (pageId.isEmpty()){
            return
        }
        loadBoard(pageId)
        withImClient {
            buildEventAndSend(
                LiveIMType.GotoPage,
                MoshiUtil.toJson(GotoPageEvent(dataEntity.pageId))
            )
        }
    }

    override fun onUploadPointData(pageId: String, pathEntity: PathEntity) {
        TopLog.i("自研白板-画线")
        dataEntity.addPath(pageId, pathEntity)
        val data = pathEntity.buildAddLineEvent(pageId)
        withImClient {
            buildEventAndSend(LiveIMType.AddLine, MoshiUtil.toJson(data))
        }
    }

    override fun onUploadEraserData(pageId: String, idList: List<String>) {
        TopLog.i("自研白板-擦除")
        dataEntity.deletePath(pageId, idList)
        val data = DeleteLinesEvent(pageId, idList)
        withImClient {
            buildEventAndSend(LiveIMType.RemoveLine, MoshiUtil.toJson(data))
        }
    }
    // endregion

    // region 接收
    override fun onUpdateBoard(serverOperationLogId: Long) {
        if (boardServerOperationId > serverOperationLogId) {
            return
        }
        val loadingBoard = boardServerOperationId != 0L
        boardServerOperationId = serverOperationLogId
        // 乐谱更新完成后或初始化完成后，会检查是否是最新乐谱
        if (!dataEntity.init || loadingBoard){
            return
        }
        launchSafe {
            getLastBoardData()
            loadBoard()
        }
    }

    override fun onPageIndexChanged(serverOperationLogId: Long, event: GotoPageEvent) {
        if (pageServerOperationId > serverOperationLogId) {
            return
        }
        if (!dataEntity.init || boardServerOperationId != 0L) {
            lastUnhandledPageId = event.targetWhiteBoardId
            pageServerOperationId = serverOperationLogId
        } else {
            loadBoard(event.targetWhiteBoardId)
        }
    }

    override fun onAddLine(serverOperationLogId: Long, event: AddLineEvent) {
        TopLog.e("websocketlog  onAddLine:${event.lineId}")
        val path = PathEntity.parseEvent(event)
        dataEntity.addPath(event.whiteBoardId, path)
        if (dataEntity.init) {
            whiteBoardView.addPointData(event.whiteBoardId, path)
        }
    }

    override fun onDeleteLine(serverOperationLogId: Long, event: DeleteLinesEvent) {
        dataEntity.deletePath(event.whiteBoardId, event.lineIds)
        if (dataEntity.init) {
            whiteBoardView.removePointData(event.lineIds)
        }
    }
    // endregion

    // region 数据同步
    private suspend fun syncAllData() {
        TopLog.i("自研白板同步历史数据")
        dataEntity.clear()
        withImClient {
            val entity = getAllBoardData(true)
            dataEntity.init(entity)
        }
        // 检查是否有IM事件没有处理
        getLastBoardData()
        dataEntity.init = true
        TopLog.i("自研白板历史数据同步完成：${dataEntity.pageId}")
    }

    private suspend fun syncPicData() {
        TopLog.i("自研白板拉取白板背景")
        withImClient {
            val entity = getAllBoardData(false, -1)
            dataEntity.updateBoard(entity.maxServerOperationLogId, entity)
        }
        TopLog.i("自研白板拉取白板背景完成")
    }

    private suspend fun getLastBoardData() {
        if (mergeUnHandlerBoardEvent()) {
            getLastBoardData()
        } else {
            mergeUnHandlerPageEvent()
        }
    }

    private suspend fun mergeUnHandlerBoardEvent(): Boolean {
        return if (boardServerOperationId != 0L && boardServerOperationId > dataEntity.curBoardServerId) {
            syncPicData()
            true
        } else {
            boardServerOperationId = 0L
            false
        }
    }

    private fun mergeUnHandlerPageEvent() {
        if (pageServerOperationId != 0L && pageServerOperationId > dataEntity.curBoardServerId) {
            if(dataEntity.gotoBoard(lastUnhandledPageId)){
                pageServerOperationId = 0
            }
        }
    }
    // endregion

    private fun loadBoard(pageId:String){
        if(!dataEntity.gotoBoard(pageId)){
            return
        }
        pageServerOperationId = 0
        loadBoard()
    }
    private fun loadBoard() {
        dataEntity.getCurPageEntity()?.let {
            whiteBoardView.loadBoard(it)
        }
        callback?.onPageIndexChanged(dataEntity.getCurPageIndex(), dataEntity.pageList.size)
    }

    private fun launchSafe(block: suspend CoroutineScope.() -> Unit){
        roomOwner.lifecycleScope.launchSafe(onError = {
            TopLog.e(it)
        }, block = block)
    }

    private inline fun withImClient(block: LiveIMClient.() -> Unit) {
        LiveIMClient.liveIMClient?.apply {
            if (imState == LiveIMClient.LOGIN_CONNECTED) {
                block(this)
            }
        }
    }

}

interface LiveBoardClientCallBack {
    fun onPageIndexChanged(index: Int, size: Int)
}