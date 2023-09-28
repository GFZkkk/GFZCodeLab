package com.gfz.whiteboard.entity

import android.graphics.Color
import android.graphics.Path
import android.graphics.Point
import android.graphics.RectF
import com.gfz.common.ext.remove
import com.gfz.common.utils.MoshiUtil
import com.xihang.module.partnertraining.live.customboard.entity.AddLineEvent

/**
 *
 * created by xueya on 2023/2/7
 */
data class LiveBoardEntity(
    var pageId: String = "",
    var pageList: MutableList<PageEntity> = mutableListOf(),
) {
    @Transient
    var init = false

    @Transient
    var curBoardServerId = 0L

    @Transient
    private var deletePathIds: MutableSet<String> = mutableSetOf()

    @Transient
    private var waitAddPathList: MutableMap<String, MutableList<PathEntity>> = mutableMapOf()

    fun init(entity: BoardListDataEntity) {
        pageId = entity.currentWhiteboardId
        pageList = entity.whiteboardList.map { data ->
            val page = getPageByData(data)
            // 添加提前收到的画线数据
            addWaitPathToPage(page)
            return@map page
        }.toMutableList()
        // 添加提前收到的擦除数据
        deletePath(deletePathIds)
        curBoardServerId = entity.maxServerOperationLogId
    }

    fun updateBoard(serverOperationLogId: Long, data: BoardListDataEntity) {
        setPage(data.whiteboardList)
        pageId = data.currentWhiteboardId
        curBoardServerId = serverOperationLogId
    }

    fun setPage(pageList: List<BoardDataEntity>){
        this.pageList = pageList.map { entity ->
            getPageEntity(entity.id)?.apply {
                background.url = entity.url
            } ?: PageEntity().apply {
                pageId = entity.id
                background = PicEntity(url = entity.url)
                addWaitPathToPage(this)
            }
        }.toMutableList()
    }

    fun clear() {
        init = false
        pageId = ""
        pageList.clear()
        deletePathIds.clear()
    }

    fun gotoBoard(newPageId: String): Boolean {
        pageList.find {
            it.pageId == newPageId
        }?.let {
            pageId = newPageId
            return true
        }
        return false
    }

    fun addPath(pageId: String, path: PathEntity) {
        if (deletePathIds.contains(path.id)) {
            return
        }
        val page = getPageEntity(pageId)
        if (page != null) {
            page.pathList.add(path)
        } else {
            saveWaitPathData(pageId, path)
        }
    }

    fun deletePath(pathIds: Collection<String>) {
        deletePathIds.addAll(pathIds)
        pageList.forEach {
            deletePath(it, deletePathIds)
        }
    }

    fun deletePath(pageId: String, pathIds: Collection<String>) {
        getPageEntity(pageId)?.let {
            deletePathIds.addAll(pathIds)
            deletePath(it, pathIds)
        }
    }

    private fun deletePath(pageEntity: PageEntity, pathIds: Collection<String>) {
        pageEntity.pathList.remove { return@remove pathIds.contains(id) }
    }

    fun getCurPageEntity(): PageEntity? {
        return getPageEntity(pageId)
    }

    fun getCurPageIndex(): Int {
        pageList.forEachIndexed { index, pageEntity ->
            if (pageEntity.pageId == pageId) {
                return index
            }
        }
        return 0
    }

    fun getPageId(index: Int): String {
        if (pageList.size <= index) {
            return ""
        }
        return pageList[index].pageId
    }

    private fun getPageEntity(pageId: String): PageEntity? {
        return pageList.find { return@find pageId == it.pageId }
    }

    private fun saveWaitPathData(pageId: String, path: PathEntity) {
        val pathList = waitAddPathList[pageId] ?: mutableListOf<PathEntity>().apply {
            waitAddPathList[pageId] = this
        }
        pathList.add(path)
    }

    private fun addWaitPathToPage(page: PageEntity) {
        val waitPathList = waitAddPathList.remove(page.pageId) ?: return
        waitPathList.remove { deletePathIds.contains(id) }
        page.pathList.addAll(waitPathList)
    }

    private fun getPageByData(boardDataEntity: BoardDataEntity): PageEntity {
        return PageEntity().apply {
            pageId = boardDataEntity.id
            background = PicEntity(url = boardDataEntity.url)
            boardDataEntity.whiteboardLineList.forEach {
                MoshiUtil.fromJson<AddLineEvent>(it.data)?.let { data ->
                    PathEntity.parseEvent(data).apply {
                        id = it.lineId
                    }
                }?.let { pathEntity ->
                    pathList.add(pathEntity)
                }
            }
        }
    }
}

data class PageEntity(
    var pageId: String = "",
    var background: PicEntity = PicEntity(),
    var pathList: MutableList<PathEntity> = mutableListOf(),
)

data class PicEntity(
    val id: String = "",
    var url: String = "",
    val width: Int = 0,
    val height: Int = 0,
)

data class PathEntity(
    var id: String = "",
    val color: String = "",
    val strokeWidth: Int = 0,
    val pointList: MutableList<Point> = mutableListOf()
) {
    companion object {
        fun parseEvent(event: AddLineEvent): PathEntity {
            return PathEntity(
                id = event.lineId,
                color = event.color,
                strokeWidth = event.strokeWidth,
                pointList = parsePointList(event.pointListStr)
            )
        }
        private fun parsePointList(pointListStr: String): MutableList<Point> {
            val pointList = mutableListOf<Point>()
            pointListStr.split(";").forEach {
                val data = it.split(",")
                if (data.size == 2) {
                    val x = data[0].toInt()
                    val y = data[1].toInt()
                    pointList.add(Point(x, y))
                }
            }
            return pointList
        }
    }

    @Transient
    val path: Path = Path()

    @Transient
    val bounds: RectF = RectF()

    @Transient
    var pathScale = 0f

    val colorInt by lazy {
        Color.parseColor(color)
    }

    fun drawByTouch(x: Float, y: Float, lastX: Float, lastY: Float, scale: Float) {
        pathTo(x, y, lastX, lastY)
        pointList.add(Point((x / scale).toInt(), (y / scale).toInt()))
    }

    fun drawByData(scale: Float): Boolean {
        if (pathScale == scale){
            return false
        }
        pathScale = scale
        var lastX = 0f
        var lastY = 0f
        pointList.forEachIndexed { index, point ->
            val x = point.x * scale
            val y = point.y * scale
            if (index == 0) {
                pathStart(x, y)
            } else {
                pathTo(x, y, lastX, lastY)
            }
            lastX = x
            lastY = y
        }
        pathEnd()
        return true
    }

    fun pathStart(x: Float, y: Float) {
        path.moveTo(x, y)
    }

    fun pathEnd() {
        path.computeBounds(bounds, true)
    }

    fun buildAddLineEvent(pageId: String): AddLineEvent {
        return AddLineEvent(
            whiteBoardId = pageId,
            lineId = id,
            color = color,
            strokeWidth = strokeWidth,
            pointListStr = buildPointString(),
        )
    }

    private fun pathTo(x: Float, y: Float, lastX: Float, lastY: Float) {
        path.quadTo(
            lastX,
            lastY,
            (x + lastX) / 2,
            (y + lastY) / 2
        )
    }

    private fun buildPointString(): String {
        return buildString {
            pointList.forEach {
                append(it.x)
                append(",")
                append(it.y)
                append(";")
            }
        }
    }
}


