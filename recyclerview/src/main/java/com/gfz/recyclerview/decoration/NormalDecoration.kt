package com.gfz.recyclerview.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gfz.lab.ext.toPX

class NormalDecoration(val bodyMargin: Int, val headMargin: Int = bodyMargin * 2, private val isHorizontal: Boolean = true) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val size = parent.adapter!!.itemCount
        val body = bodyMargin / 2
        val head = headMargin
        if (isHorizontal) {
            when (position) {
                0 -> {
                    outRect.left = head
                    outRect.right = body
                }
                size - 1 -> {
                    outRect.left = body
                    outRect.right = head
                }
                else -> {
                    outRect.left = body
                    outRect.right = body
                }
            }
        } else {
            when (position) {
                0 -> {
                    outRect.top = head
                    outRect.bottom = body
                }
                size - 1 -> {
                    outRect.top = body
                    outRect.bottom = head
                }
                else -> {
                    outRect.top = body
                    outRect.bottom = body
                }
            }
        }
    }
}