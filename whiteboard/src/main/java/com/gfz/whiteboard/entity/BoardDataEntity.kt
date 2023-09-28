package com.gfz.whiteboard.entity

import com.squareup.moshi.JsonClass

/**
 *
 * created by xueya on 2023/2/20
 */
@JsonClass(generateAdapter = true)
data class BoardListDataEntity(
    val currentWhiteboardId: String = "",
    val maxServerOperationLogId: Long = 0,
    val whiteboardList: List<BoardDataEntity> = listOf(),
)

@JsonClass(generateAdapter = true)
data class BoardDataEntity(
    val id: String = "",
    val url: String = "",
    val whiteboardLineList: List<LineDataEntity> = listOf(),
)

@JsonClass(generateAdapter = true)
data class LineDataEntity(
    val lineId: String = "",
    val data: String = "",
)
