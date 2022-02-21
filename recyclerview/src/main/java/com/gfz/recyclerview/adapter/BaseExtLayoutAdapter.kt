package com.gfz.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.gfz.common.utils.TopLog
import kotlin.collections.ArrayList
import com.gfz.recyclerview.adapter.BaseRecyclerViewHolder as BaseRecyclerViewHolder

/**
 *
 * created by gaofengze on 2021/1/27
 */
abstract class BaseExtLayoutAdapter<T>(list: List<T?> = ArrayList()) :
    BaseRecyclerViewAdapter<T>(list) {

    val EMPTY = -1
    val FOOT = -2
    val HEAD = -3

    var footerViewBinding: ViewBinding? = null
    var emptyViewBinding: ViewBinding? = null
    var headerViewBinding: ViewBinding? = null

    private val extViewNotifyHelper by lazy {
        ExtViewNotifyHelper()
    }

    override fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<T> {
        val holder: BaseRecyclerViewHolder<T>? = when (viewType) {
            EMPTY -> getEmptyViewHolder(layoutInflater, parent) as? BaseRecyclerViewHolder<T>
            FOOT -> getFooterViewHolder(layoutInflater, parent) as? BaseRecyclerViewHolder<T>
            HEAD -> getHeaderViewHolder(layoutInflater, parent) as? BaseRecyclerViewHolder<T>
            else -> onCreateDataViewHolder(layoutInflater, parent, viewType)
        }
        return requireNotNull(holder) {
            TopLog.e("view holder is null")
        }
    }

    abstract fun onCreateDataViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<T>

    override fun getItemCount(): Int {
        return getEmptyNum() + getHeaderNum() + getDataItemCount() + getFooterNum()
    }

    override fun getItemViewType(position: Int): Int {
        if (isEmptyView()) return EMPTY
        if (isHeadView(position)) return HEAD
        return if (isFootView(position)) FOOT else getEFItemViewType(getDataPosition(position))
    }

    /**
     * 如果是GridLayoutManager布局，空布局需要独占一行
     * 重写该方法时注意处理
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isDataPosition(position)) {
                        1
                    } else manager.spanCount
                }
            }
        }
    }

    //针对流式布局
    override fun onViewAttachedToWindow(holder: BaseRecyclerViewHolder<T>) {
        val layoutPosition: Int = holder.layoutPosition
        if (isExtView(layoutPosition)) {
            val layoutParams: ViewGroup.LayoutParams = holder.itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                //占领全部空间;
                layoutParams.isFullSpan = true
            }
        }
    }

    // region 数据

    override fun getData(position: Int): T? {
        return super.getData(getDataPosition(position))
    }

    override fun addAll(data: List<T?>) {
        addAll(data, getHeaderNum() + dataSize)
    }

    override fun add(data: T, block: () -> Unit) {
        add(data, getHeaderNum() + dataSize, block)
    }

    override fun addAllData(dataList: List<T?>?, position: Int) {
        super.addAllData(dataList, position - getHeaderNum())
    }

    override fun addData(data: T?, position: Int) {
        super.addData(data, position - getHeaderNum())
    }

    override fun removeData(position: Int) {
        super.removeData(getDataPosition(position))
    }

    override fun setData(position: Int, data: T?) {
        super.setData(getDataPosition(position), data)
    }

    override fun notifyDataRangeInsert(position: Int, length: Int, block: () -> Unit) {
        extViewNotifyHelper.notifyExtItemChange(block) {
            notifyItemRangeInserted(position + headerStatus.moveRange.length, length)
        }
        /*block()
        notifyItemRangeInserted(position, length)*/
    }

    override fun notifyDataRangeRemove(position: Int, length: Int, block: () -> Unit) {
        extViewNotifyHelper.notifyExtItemChange(block) {
            notifyItemRangeRemoved(position + headerStatus.moveRange.length, length)
        }
    }

    override fun notifyDataRangeChange(position: Int, length: Int, block: () -> Unit) {
        extViewNotifyHelper.notifyExtItemChange(block) {
            notifyItemRangeChanged(position + headerStatus.moveRange.length, length)
        }
    }

    /**
     * 额外布局刷新
     */
    inner class ExtViewNotifyHelper {
        var emptyStatus: RangeStatus = EmptyRangeStatus()
            private set
        var headerStatus: RangeStatus = HeaderRangeStatus()
            private set
        var footerStatus: RangeStatus = FooterRangeStatus()
            private set

        // 通知额外布局刷新
        fun notifyExtItemChange(
            block: () -> Unit,
            notifyDataChange: ExtViewNotifyHelper.() -> Unit
        ) {
            // 记录，更新数据
            recordRangeBeforeChange()
            block()
            recordRangeAfterChange()
            // 更新视图
            notifyItemMoveByRange(emptyStatus)
            notifyItemMoveByRange(headerStatus)
            notifyDataChange(this)
            notifyItemMoveByRange(footerStatus)
        }

        // 通知额外布局变化
        private fun notifyItemMoveByRange(range: RangeStatus) {
            with(range.moveRange) {
                if (length > 0) {
                    notifyItemRangeInserted(start, length)
                } else if (length < 0) {
                    notifyItemRangeRemoved(start, -length)
                }
            }
        }

        // 记录数据变化之前
        private fun recordRangeBeforeChange() {
            emptyStatus.recordOld()
            headerStatus.recordOld()
            footerStatus.recordOld()
        }

        // 记录数据变化之后
        private fun recordRangeAfterChange() {
            emptyStatus.recordNew()
            headerStatus.recordNew()
            footerStatus.recordNew()
        }

        inner class EmptyRangeStatus() : RangeStatus() {
            override fun buildRange(): Range {
                return Range(0, getEmptyNum())
            }
        }

        inner class HeaderRangeStatus() : RangeStatus() {
            override fun buildRange(): Range {
                return Range(0, getHeaderNum())
            }
        }

        inner class FooterRangeStatus() : RangeStatus() {
            override fun buildRange(): Range {

                return Range(
                    if (dataSize == 0) {
                        getEmptyNum()
                    } else {
                        getHeaderNum() + dataSize
                    },
                    getFooterNum()
                )
            }
        }

        abstract inner class RangeStatus {
            lateinit var oldRange: Range
                private set
            lateinit var newRange: Range
                private set
            lateinit var moveRange: Range
                private set

            fun recordOld() {
                oldRange = buildRange()
            }

            fun recordNew() {
                newRange = buildRange()
                mergeRange()
            }

            // 合并变化前后的range
            private fun mergeRange() {
                val unChangeLength = newRange.length.coerceAtMost(oldRange.length)
                val start = newRange.start + unChangeLength
                val moveLength = newRange.length - oldRange.length
                moveRange = Range(start, moveLength)
            }

            abstract fun buildRange(): Range
        }
    }

    data class Range(val start: Int = 0, val length: Int = 0)

    // endregion

    // region 工具方法
    /**
     * 根据列表中的位置获取数据中的位置
     * 如果是扩展类型则不做处理
     */
    open fun getDataPosition(viewPosition: Int): Int {
        var dataPosition = viewPosition
        if (isDataPosition(viewPosition)) {
            if (isHaveHead()) {
                dataPosition = viewPosition - getHeaderNum()
            }
        }
        return dataPosition
    }

    /**
     * 获取数据长度
     */
    open fun getDataItemCount(): Int {
        return dataSize
    }

    protected open fun getEFItemViewType(position: Int): Int {
        return 0
    }

    protected open fun getHeaderNum(): Int {
        return if (isHaveHead() && getDataItemCount() > 0) 1 else 0
    }

    protected open fun getFooterNum(): Int {
        return if (isHaveFoot() && getDataItemCount() > 0) 1 else 0
    }

    protected fun getEmptyNum(): Int {
        return if (isHaveEmpty() && getDataItemCount() == 0) 1 else 0
    }
    // endregion

    // region 判断方法
    /**
     * 是否是数据的类型
     */
    protected open fun isDataViewType(viewType: Int): Boolean {
        return viewType >= 0
    }

    /**
     * 是否是数据的位置
     */
    protected open fun isDataPosition(adapterPosition: Int): Boolean {
        return !isExtView(adapterPosition)
    }

    /**
     * 是否是空布局
     */
    protected open fun isEmptyView(): Boolean {
        return isHaveEmpty() && getDataItemCount() == 0
    }

    /**
     * 是否是足布局
     */
    protected open fun isFootView(adapterPosition: Int): Boolean {
        return isHaveFoot() && getHeaderNum() + getDataItemCount() == adapterPosition
    }

    /**
     * 是否是头布局
     */
    protected open fun isHeadView(adapterPosition: Int): Boolean {
        return isHaveHead() && adapterPosition == 0
    }

    protected open fun isExtView(adapterPosition: Int): Boolean {
        return isHeadView(adapterPosition) || isFootView(adapterPosition) || isEmptyView()
    }
    // endregion

    // region 额外布局
    /**
     * 是否有足布局
     */
    protected open fun isHaveHead(): Boolean {
        return headerViewBinding != null || getHeaderViewBindingClass() != null
    }

    /**
     * 是否有头布局
     */
    protected open fun isHaveFoot(): Boolean {
        return footerViewBinding != null || getFooterViewBindingClass() != null
    }

    /**
     * 是否有空布局
     */
    protected open fun isHaveEmpty(): Boolean {
        return emptyViewBinding != null || getEmptyViewBindingClass() != null
    }

    protected open fun getEmptyViewBindingClass(): Class<*>? {
        return null
    }

    protected open fun getFooterViewBindingClass(): Class<*>? {
        return null
    }

    protected open fun getHeaderViewBindingClass(): Class<*>? {
        return null
    }

    open fun getHeaderViewHolder(
        layoutInflater: LayoutInflater?,
        parent: ViewGroup
    ): HeaderViewHolder<*>? {
        if (headerViewBinding == null) {
            headerViewBinding =
                getViewBinding(getHeaderViewBindingClass(), layoutInflater, parent)
        }
        return headerViewBinding?.let {
            HeaderViewHolder(it)
        }
    }

    open fun getFooterViewHolder(
        layoutInflater: LayoutInflater?,
        parent: ViewGroup
    ): FooterViewHolder<*>? {
        if (footerViewBinding == null) {
            footerViewBinding = getViewBinding(getFooterViewBindingClass(), layoutInflater, parent)
        }
        return footerViewBinding?.let {
            FooterViewHolder(it)
        }
    }

    open fun getEmptyViewHolder(
        layoutInflater: LayoutInflater?,
        parent: ViewGroup
    ): EmptyViewHolder<ViewBinding>? {
        if (emptyViewBinding == null) {
            emptyViewBinding =
                getViewBinding(getEmptyViewBindingClass(), layoutInflater, parent)
        }
        return emptyViewBinding?.let {
            EmptyViewHolder(it)
        }
    }

    open fun <VB : ViewBinding> getViewBinding(vbClass: Class<*>?, parent: ViewGroup): VB {
        return getViewBinding(vbClass, LayoutInflater.from(parent.context), parent)
    }

    protected open fun <VB : ViewBinding> getViewBinding(
        vbClass: Class<*>?,
        layoutInflater: LayoutInflater?,
        parent: ViewGroup
    ): VB {
        return vbClass?.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.javaPrimitiveType
        )?.invoke(null, layoutInflater, parent, false) as VB
    }


    //数据viewholder需要继承的布局
    abstract class DataViewHolder<T, VB : ViewBinding>(binding: VB) :
        BaseVBRecyclerViewHolder<T, VB>(binding) {
    }

    //扩展ViewHolder需要继承的布局
    open class ExtViewHolder<VB : ViewBinding>(binding: VB) :
        BaseVBRecyclerViewHolder<Any?, VB>(binding) {

        override fun onBindViewHolder(data: Any?, position: Int) {

        }

    }

    class EmptyViewHolder<VB : ViewBinding>(binding: VB) : ExtViewHolder<VB>(binding)

    class FooterViewHolder<VB : ViewBinding>(binding: VB) : ExtViewHolder<VB>(binding)

    class HeaderViewHolder<VB : ViewBinding>(binding: VB) : ExtViewHolder<VB>(binding)
    // endregion
}