package com.xihang.module.partnertraining.live.customboard.entity

import com.squareup.moshi.JsonClass
import com.gfz.whiteboard.LiveIMType

/**
 *
 * created by xueya on 2023/2/20
 */
data class LiveIMEventEntity(
    val clientOperationId: String = "",
    val timestamp: Long = 0L,
    var whiteboardRoomId: String = "",
    var type: LiveIMType = LiveIMType.Unknown,
    var data: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "clientOperationId" to clientOperationId,
            "whiteboardRoomId" to whiteboardRoomId,
            "timestamp" to timestamp,
            "type" to type.type,
            "data" to data,
        )
    }
}

@JsonClass(generateAdapter = true)
data class AddLineEvent(
    val whiteBoardId: String,
    val lineId: String = "",
    val color: String = "",
    val strokeWidth: Int = 0,
    var pointListStr: String = "",
)

@JsonClass(generateAdapter = true)
data class DeleteLinesEvent(
    val whiteBoardId: String,
    val lineIds: List<String> = listOf()
)

@JsonClass(generateAdapter = true)
data class GotoPageEvent(
    val targetWhiteBoardId: String = ""
)

