package com.gfz.recyclerview.adapter

import android.util.SparseBooleanArray
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.gfz.recyclerview.bean.BaseMultipleChooseBean

/**
 * 多选recyclerview 基类
 * 理念上不支持反选，以选中为优先
 * created by gaofengze on 2020/4/14
 */

abstract class BaseMultipleChooseAdapter<T: BaseMultipleChooseBean>(dataList: List<T?> = ArrayList()) :
    BaseRecyclerViewAdapter<T>(dataList){

    private val chooseItem: SparseBooleanArray = SparseBooleanArray()
    private val chooseTitleItem: SparseBooleanArray by lazy {
        SparseBooleanArray()
    }

    /**
     * 是否根据view确定多选框范围
     */
    private var bound: ChooseBound? = null
    private var correction: Int = 0
    private var sureBoundByView = true

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
    fun checkBound(x: Float, position: Int, itemView: View?): Boolean{
        val chooseView: View? = itemView?.findViewById(getChooseViewByPosition(position))
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
    /**
     * 是否是新的一组
     */
    fun isNewGroup(position: Int): Boolean =
        if (position == 0)
            true
        else
            getData(position)?.getBaseGroupId() != getData(position - 1)?.getBaseGroupId()

    fun setTitleItemStatus(position: Int, change: Boolean){
        val groupId = getGroupIdByPosition(position)
        chooseTitleItem.append(groupId, change)
        getData().forEachIndexed { index, t ->
            if (t?.getBaseGroupId() == groupId && isNewGroup(index)){
                notifyItemChanged(index)
                return@forEachIndexed
            }
        }
    }

    fun updateMultipleItem(position: Int){
        val groupId = getGroupIdByPosition(position)
        val change = isMultipleChooseItem(position)
        getData().forEachIndexed { index, multipleChooseBean ->
            if (multipleChooseBean?.getBaseGroupId() == groupId){
                if (change != isChooseItem(index)){
                    chooseItem(index, change)
                }
            }
        }
    }

    /**
     * 改变组的修改状态
     */
    fun changeGroupChooseStatus(position: Int){
        setTitleItemStatus(position, !isMultipleChooseItem(position))
        updateMultipleItem(position)
    }

    fun checkGroupCheckStatus(position: Int){
        val groupId = getGroupIdByPosition(position)
        val multipleChoose: Boolean = isMultipleChooseItem(position)
        var allChoose = true
        getData().forEachIndexed { index, t ->
            if (t?.getBaseGroupId() == groupId){
                if (!isChooseItem(index)){
                    allChoose = false
                    return@forEachIndexed
                }

            }
        }
        if (multipleChoose){
            // 是全选状态，有未选中的，取消全选状态
            if (!allChoose){
                setTitleItemStatus(position, false)
            }
        }else{
            // 不是全选状态，但是全部都被选中，修改全选状态为全选
            if (allChoose){
                setTitleItemStatus(position, true)
            }
        }

    }

    fun isMultipleChooseItem(position: Int): Boolean {
        val groupId = getGroupIdByPosition(position)
        return chooseTitleItem.get(groupId, false)
    }

    fun getGroupIdByPosition(position: Int) = getData(position)!!.getBaseGroupId()
    // endregion

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