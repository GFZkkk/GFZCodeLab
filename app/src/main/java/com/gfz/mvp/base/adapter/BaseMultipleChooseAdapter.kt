package com.gfz.mvp.base.adapter

import android.util.SparseBooleanArray
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.gfz.mvp.utils.TopLog

/**
 * 多选recyclerview 基类
 * 理念上不支持反选，以选中为优先
 * created by gaofengze on 2020/4/14
 */

abstract class BaseMultipleChooseAdapter<T>(dataList: List<T?> = ArrayList(), clickIndex: Int = -1, layoutId: Int) :
    BaseRecyclerViewAdapter<T>(dataList, clickIndex, layoutId){

    private val chooseItem: SparseBooleanArray = SparseBooleanArray()

    /**
     * 是否根据view确定多选框范围
     */
    private var bound: ChooseBound? = null
    private var correction: Int = 0
    private var sureBoundByView = true

    constructor(dataList: List<T?> = ArrayList(), clickIndex: Int = -1): this(dataList, clickIndex, 0)

    /**
     * 设置固定的选择范围
     */
    fun setBound(left: Int, right: Int, flag: Int = 0) {
        this.bound = ChooseBound(left, right, flag)
        sureBoundByView = false
    }

    /**
     * 设置固定的选择范围
     */
    fun setBound(correction: Int) {
        this.correction = correction
        sureBoundByView = true
    }

    /**
     * 选项的范围
     */
    fun checkBound(x: Float, position: Int, view: View?): Boolean{
        val chooseView: View? = view?.findViewById(getChooseViewByPosition(position))
        chooseView?.let{
            return isBound(x, ChooseBound(chooseView.left, chooseView.right, correction))
        }
        return false
    }

    /**
     * 选项的范围
     */
    fun checkBound(x: Float): Boolean{
        bound?.let {
            return isBound(x, it)
        }
        return false
    }

    /**
     * 是否在选中范围内
     */
    private fun isBound(x: Float, bound: ChooseBound): Boolean{
        return x >= bound.left - bound.flag && x <= bound.right + bound.flag
    }

    /**
     * 改变item的选中状态
     * @param position item的位置
     */
    fun chooseItem(position: Int) {
        chooseItem(position, !isMultipleChooseItem(position))
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
        }
    }

    /**
     * 是否被多选
     */
    fun isMultipleChooseItem(position: Int): Boolean {
        return if (isDataIndex(position)) {
            chooseItem[position]
        } else false
    }

    fun isCheckBoundByView() = sureBoundByView

    /**
     * 根据[position]获取该item的分组，当不允许跨组多选的时候需要重写
     */
    open fun getGroupId(position: Int): Int {
        return 0
    }

    /**
     * 获取多选范围的viewId
     */
    open fun getChooseViewByPosition(position: Int): Int = 0

    override fun setClickIndex(clickIndex: Int) {
        chooseItem(clickIndex)
        super.setClickIndex(clickIndex)

    }

    /**
     * 防止item多次更新闪烁
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }

    data class ChooseBound(val left: Int, val right: Int, val flag: Int = 0)

}