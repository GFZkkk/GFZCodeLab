package com.gfz.recyclerview.adapter

import android.graphics.Rect
import android.util.SparseBooleanArray
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gfz.common.utils.TopLog
import com.gfz.recyclerview.bean.BaseMultipleChooseBean

/**
 * 多选recyclerview 基类
 * 全选时优先选中
 * created by gaofengze on 2020/4/14
 */

abstract class BaseMultipleChooseAdapter<T : BaseMultipleChooseBean>(dataList: List<T?> = ArrayList()) :
    BaseRecyclerViewAdapter<T>(dataList) {

    private val chooseItem: SparseBooleanArray = SparseBooleanArray()
    private val chooseTitleItem: SparseBooleanArray by lazy {
        SparseBooleanArray()
    }
    private val layoutManager get() = mRecyclerview.layoutManager
    private lateinit var mRecyclerview: RecyclerView

    /**
     * 是否根据view确定多选框范围
     */
    private var bound: Rect? = null
    private var correction: Int = 0
    private var enableTouchCheck = false

    private var currentPosition = -1
    private var groupId = -1
    private var check = false

    /**
     * 设置固定的选择范围
     */
    fun setRectBound(rect: Rect) {
        this.bound = rect
        enableTouchCheck = true
    }

    /**
     * 设置view额外的选择范围
     */
    fun setViewBound(correction: Int) {
        this.correction = correction
        enableTouchCheck = true
    }

    /**
     * 根据[position]获取该item的分组，当不允许跨组多选的时候需要重写
     */
    open fun getGroupId(position: Int): Int {
        return 0
    }

    /**
     * 获取多选范围的viewId
     */
    open fun getCheckBoxIdByPosition(position: Int): Int = 0

    override fun setClickIndex(clickIndex: Int) {
        chooseItem(clickIndex)
        super.setClickIndex(clickIndex)

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerview = recyclerView.apply {
            if (enableTouchCheck) {
                setOnTouchListener { _, e ->
                    return@setOnTouchListener handleTouchCheck(e)
                }
            }
        }
    }

    private fun handleTouchCheck(e: MotionEvent): Boolean {
        if (!enableTouchCheck || layoutManager == null) {
            return false
        }
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                val realPosition = getTouchCheckBoxIndex(e.x, e.y)
                if (realPosition != -1) {
                    if (currentPosition != realPosition) {
                        //记录第一个选中的item的groupId
                        if (groupId == -1) {
                            groupId = getGroupId(realPosition)
                            check = isChooseItem(realPosition)
                        }
                        //只有同组的item才会在一次滑动中选中
                        if (groupId == getGroupId(realPosition)) {
                            chooseItem(realPosition, !check)
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
        return false
    }

    private fun getTouchCheckBoxIndex(x: Float, y: Float): Int {
        val view = mRecyclerview.findChildViewUnder(x, y) ?: return -1
        val position = mRecyclerview.getChildAdapterPosition(view)
        val rect = bound ?: view.findViewById<View>(getCheckBoxIdByPosition(position))?.let {
            Rect(it.left, it.top, it.right, it.bottom).apply {
                inset(-correction, -correction)
            }
        }
        val rx = (x - view.left).toInt()
        val ry = (y - view.top).toInt()
        val hit = rect?.contains(rx, ry) ?: false
        return if (hit) {
            position
        } else {
            -1
        }
    }

    // region 单选
    /**
     * 改变item的选中状态
     * @param position item的位置
     */
    fun chooseItem(position: Int) {
        chooseItem(position, !isChooseItem(position))
    }

    /**
     * 改变item的选中状态
     * @param position item的位置
     * @param choose 选中还是取消选中
     */
    fun chooseItem(position: Int, choose: Boolean) {
        if (isDataIndex(position)) {
            chooseItem.append(position, choose)
            notifyItemChanged(position)
            checkGroupCheckStatus(position)
        }
    }

    /**
     * 是否被选中
     */
    fun isChooseItem(position: Int): Boolean {
        return if (isDataIndex(position)) {
            chooseItem.get(position, false)
        } else false
    }
    // endregion

    // region 多选
    fun isMultipleChooseItem(position: Int): Boolean {
        val groupId = getGroupIdByPosition(position)
        return chooseTitleItem.get(groupId, false)
    }

    /**
     * 是否是新的一组
     */
    fun isNewGroup(position: Int): Boolean =
        if (position == 0)
            true
        else
            getData(position)?.getBaseGroupId() != getData(position - 1)?.getBaseGroupId()

    /**
     * 改变组的修改状态
     */
    fun changeGroupChooseStatus(position: Int) {
        setTitleItemStatus(position, !isMultipleChooseItem(position))
        updateMultipleItem(position)
    }

    private fun setTitleItemStatus(position: Int, change: Boolean) {
        val groupId = getGroupIdByPosition(position)
        chooseTitleItem.append(groupId, change)
        getDataList().forEachIndexed { index, t ->
            if (t?.getBaseGroupId() == groupId && isNewGroup(index)) {
                notifyItemChanged(index)
                return@forEachIndexed
            }
        }
    }

    private fun updateMultipleItem(position: Int) {
        val groupId = getGroupIdByPosition(position)
        val change = isMultipleChooseItem(position)
        getDataList().forEachIndexed { index, multipleChooseBean ->
            if (multipleChooseBean?.getBaseGroupId() == groupId) {
                if (change != isChooseItem(index)) {
                    chooseItem(index, change)
                }
            }
        }
    }

    private fun checkGroupCheckStatus(position: Int) {
        val groupId = getGroupIdByPosition(position)
        val multipleChoose: Boolean = isMultipleChooseItem(position)
        var allChoose = true
        getDataList().forEachIndexed { index, t ->
            if (t?.getBaseGroupId() == groupId) {
                if (!isChooseItem(index)) {
                    allChoose = false
                    return@forEachIndexed
                }

            }
        }
        if (multipleChoose) {
            // 是全选状态，有未选中的，取消全选状态
            if (!allChoose) {
                setTitleItemStatus(position, false)
            }
        } else {
            // 不是全选状态，但是全部都被选中，修改全选状态为全选
            if (allChoose) {
                setTitleItemStatus(position, true)
            }
        }

    }

    private fun getGroupIdByPosition(position: Int) = getData(position)!!.getBaseGroupId()
    // endregion

}