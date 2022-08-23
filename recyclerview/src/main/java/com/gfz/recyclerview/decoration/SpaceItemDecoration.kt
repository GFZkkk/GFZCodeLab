package com.gfz.recyclerview.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gfz.common.utils.TopLog

/**
 * 控制列表item之间间隔
 *
 * padding 指item内部之间的间隔
 * margin 指外侧item的间隔
 * H 指水平方向间隔
 * V 指竖直方向间隔
 * titlePadding 指独占一行的item与上一行item之间的间隔
 *
 * created by xueya on 2022/3/30
 */
class SpaceItemDecoration(
    marginH: Int = 0,
    marginV: Int = 0,
    private val paddingH: Int = 0,
    private val paddingV: Int = 0,
    var marginTop: Int = 0,
    var marginBottom: Int = 0,
    var marginLeft: Int = 0,
    var marginRight: Int = 0,
    private val titlePaddingV: Int = 0
) : RecyclerView.ItemDecoration() {

    init {
        if (marginTop == 0){
            marginTop = marginV
        }
        if (marginBottom == 0){
            marginBottom = marginV
        }
        if (marginLeft == 0){
            marginLeft = marginH
        }
        if (marginRight == 0){
            marginRight = marginH
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        parent.layoutManager?.let {
            val position = parent.getChildAdapterPosition(view)
            val size = parent.adapter!!.itemCount
            when (it) {
                is GridLayoutManager -> gridOffsets(outRect, it, position, size)
                is LinearLayoutManager -> linearOffsets(outRect, it, position, size)
            }
        }
    }

    private fun linearOffsets(outRect: Rect, layoutManager: LinearLayoutManager, position: Int, size: Int) {
        val isHorizontal = layoutManager.orientation == RecyclerView.HORIZONTAL
        if (isHorizontal) {
            val body = paddingH / 2
            when (position) {
                0 -> {
                    outRect.left = marginLeft
                    outRect.right = body
                }
                size - 1 -> {
                    outRect.left = body
                    outRect.right = marginRight
                }
                else -> {
                    outRect.left = body
                    outRect.right = body
                }
            }
        } else {
            val body = paddingV / 2
            when (position) {
                0 -> {
                    outRect.top = marginTop
                    outRect.bottom = body
                }
                size - 1 -> {
                    outRect.top = body
                    outRect.bottom = marginBottom
                }
                else -> {
                    outRect.top = body
                    outRect.bottom = body
                }
            }
        }
    }

    private fun gridOffsets(outRect: Rect, layoutManager: GridLayoutManager, position: Int, size: Int) {
        if (layoutManager.spanSizeLookup.getSpanSize(position) == layoutManager.spanCount){
            if (position > 0){
                outRect.top = titlePaddingV
            }
            return
        }
        val maxIndex = layoutManager.spanCount - 1
        val index = layoutManager.spanSizeLookup.getSpanIndex(position, layoutManager.spanCount)
        val group = layoutManager.spanSizeLookup.getSpanGroupIndex(position, layoutManager.spanCount)
        val maxGroup = layoutManager.spanSizeLookup.getSpanGroupIndex(size, layoutManager.spanCount)
        outRect.apply {
            left = if(index == 0){
                marginLeft
            } else {
                paddingH / 2
            }
            right = if(index == maxIndex) {
                marginRight
            } else {
                paddingH / 2
            }
            top = if (group == 0 || isTitle(layoutManager, position - index - 1)){
                marginTop
            } else {
                paddingV / 2
            }
            bottom = if (group == maxGroup || isTitle(layoutManager, position - index + maxIndex + 1)){
                marginBottom
            } else {
                paddingV / 2
            }
        }
    }

    private fun isTitle(layoutManager: GridLayoutManager, position: Int): Boolean{
        return layoutManager.spanSizeLookup.getSpanSize(position) == layoutManager.spanCount
    }
}

