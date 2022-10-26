package com.gfz.lab.ui.fragment.home.whiteboard.entity

import android.graphics.Path
import android.graphics.RectF


/**
 *
 * created by xueya on 2022/10/25
 */
data class PathEntity(
    val path: Path = Path(),
    val range: RectF = RectF(),
    val color: Int,
    val strokeWidth: Float,
)