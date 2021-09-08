package com.gfz.lab.base.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gfz.lab.utils.TopLog
import com.gfz.lab.utils.toPX

/**
 *
 * created by gaofengze on 2021/5/14
 */
class NormalItemDecoration(private val screenWidth: Int, private val count: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val width = 50.toPX(parent.context)
        TopLog.e(width)
        val leftWidth = screenWidth - width * count
        val blankWidth = leftWidth / (count + 1)
        val position = parent.getChildAdapterPosition(view)
        if (position == 0){
            outRect.left = blankWidth
        }
        outRect.right = blankWidth
    }

}