package com.gfz.lab.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gfz.recyclerview.adapter.BaseMultipleChooseAdapter

/**
 * 配合可多选recyclerViewAdapter使用
 * 拦截选项处的滑动事件
 * created by gaofengze on 2020/4/14
 */

class MultiRecyclerView : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private var currentPosition = -1
    private var groupId = -1
    private var check = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        val linearLayoutManager: LinearLayoutManager? = layoutManager as LinearLayoutManager?
        val multipleChooseAdapter: BaseMultipleChooseAdapter<*>? = adapter as BaseMultipleChooseAdapter<*>?
        if (linearLayoutManager != null && multipleChooseAdapter != null){
            val currentFisrtPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()
            when (e.action) {
                MotionEvent.ACTION_MOVE -> {
                    val x = e.rawX
                    val checkBound: Boolean = if (multipleChooseAdapter.isCheckBoundByView()){
                        multipleChooseAdapter.checkBound(x, currentFisrtPosition, linearLayoutManager.findViewByPosition(currentFisrtPosition))
                    }else{
                        multipleChooseAdapter.checkBound(x)
                    }
                    if (checkBound) {
                        val y = e.y.toInt()
                        var realPosition = currentFisrtPosition
                        for (i in currentFisrtPosition..multipleChooseAdapter.itemCount){
                            val view1: View? = linearLayoutManager.findViewByPosition(i)
                            view1?.let{
                                if (it.top <= y && it.bottom >= y) {
                                    realPosition = i
                                }
                            }
                            if (realPosition != currentFisrtPosition){
                                break
                            }
                        }
                        if (currentPosition != realPosition) {
                            //记录第一个选中的item的groupId
                            if (groupId == -1) {
                                groupId = multipleChooseAdapter.getGroupId(realPosition)
                                check = multipleChooseAdapter.isChooseItem(realPosition)
                            }
                            //只有同组的item才会在一次滑动中选中
                            if (groupId == multipleChooseAdapter.getGroupId(realPosition)) {
                                multipleChooseAdapter.chooseItem(realPosition, !check)
                            }
                            currentPosition = realPosition
                        }
                        return true
                    }
                }
                MotionEvent.ACTION_UP -> {
                    groupId = -1
                    currentPosition = -1
                }
            }
        }

        return super.onTouchEvent(e)
    }
}