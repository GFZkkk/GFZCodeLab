package com.gfz.lab.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlin.math.max

/**
 * Created by gaofengze on 2022/1/7
 */
class FlowLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        val paddingH = paddingLeft + paddingRight
        val childEnd = width - paddingH

        var lineWidth = 0
        var lineMaxHeight = 0

        var realWidth = 0
        var realHeight = 0

        for (index in 0 until childCount) {

            val childView = getChildAt(index)

            if (childView.visibility == GONE) {
                continue
            }

            measureChild(childView, widthMeasureSpec, heightMeasureSpec)

            val childLayoutParams = childView.layoutParams as MarginLayoutParams
            val childWidth = childView.measuredWidth.plus(
                childLayoutParams.leftMargin + childLayoutParams.rightMargin
            )
            val childHeight = childView.measuredHeight.plus(
                childLayoutParams.topMargin + childLayoutParams.bottomMargin
            )
            // 检查是否需要换行
            if (lineWidth + childWidth > childEnd) {
                // 宽度取最宽
                realWidth = max(lineWidth, realWidth)
                // 高度取末尾item高度相加
                realHeight += lineMaxHeight
                // reset
                lineMaxHeight = 0
                // 记录没有放下的item的宽度
                lineWidth = childWidth
            } else {
                lineWidth += childWidth
            }
            lineMaxHeight = max(lineMaxHeight, childHeight)
        }

        realHeight += lineMaxHeight

        realWidth += paddingH
        realHeight += paddingTop + paddingBottom

        val finalWidth = if (widthMode == MeasureSpec.AT_MOST) realWidth else width
        val finalHeight = if (heightMode == MeasureSpec.AT_MOST) realHeight else height

        setMeasuredDimension(finalWidth, finalHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        val childEnd = measuredWidth - paddingRight

        var left = 0
        var right = 0
        var top = 0
        var bottom = 0

        var lineHeight = paddingTop
        var lineWidth = paddingLeft
        var lineMaxHeight = 0

        for (index in 0 until childCount) {
            val view = getChildAt(index)

            if (view.visibility == GONE) {
                continue
            }

            val width = view.measuredWidth
            val height = view.measuredHeight
            val marginLayoutParams = view.layoutParams as MarginLayoutParams

            left = lineWidth + marginLayoutParams.leftMargin
            right = left + width

            if (right > childEnd) {
                // 换行
                lineHeight += lineMaxHeight
                // reset
                lineMaxHeight = 0
                lineWidth = paddingLeft
                // 重新计算
                left = lineWidth + marginLayoutParams.leftMargin
                right = left + width
            }
            top = lineHeight + marginLayoutParams.topMargin
            bottom = top + height

            view.layout(left, top, right, bottom)

            lineWidth = right + marginLayoutParams.leftMargin

            lineMaxHeight = max(
                lineMaxHeight,
                bottom - top + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}