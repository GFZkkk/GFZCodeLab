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
abstract class BaseExtLayoutAdapter<T> : BaseRecyclerViewAdapter<T>() {

    val EMPTY = -1
    val FOOT = -2
    val HEAD = -3

    var emptyViewBinding: ViewBinding? = null
    var footerViewBinding: ViewBinding? = null
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
        return holder ?: UnknownViewHolder(layoutInflater, parent)
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
        if (isHeadView(position)) return HEAD
        if (isFootView(position)) return FOOT
        return if (isEmptyView()) return EMPTY else getEFItemViewType(getDataPosition(position))
    }

    /**
     * 如果是GridLayoutManager布局，空布局需要独占一行
     * 重写该方法时注意处理
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
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

    override fun notifyDataAllChanged(block: () -> Unit) {
        val oldLength = length
        extViewNotifyHelper.notifyExtItemChange(block) {
            val newLength = length
            notifyItemRangeChanged(getHeaderNum(), oldLength.coerceAtMost(newLength))
            if (oldLength > newLength) {
                notifyItemRangeRemoved(getHeaderNum() + newLength, oldLength - newLength)
            } else if (oldLength < newLength) {
                notifyItemRangeInserted(getHeaderNum() + oldLength, newLength - oldLength)
            }
        }
    }

    // 因数据变化可能会引起额外布局变化
    override fun notifyDataRangeInserted(position: Int, block: () -> Unit) {
        extViewNotifyHelper.notifyExtItemChange(block) {
            notifyItemRangeInserted(position + headerStatus.moveRange.length, length)
        }
    }

    // 因数据变化可能会引起额外布局变化
    override fun notifyDataRangeRemoved(position: Int, block: () -> Unit) {
        extViewNotifyHelper.notifyExtItemChange(block) {
            notifyItemRangeRemoved(position + headerStatus.moveRange.length, length)
        }
    }

    fun notifyExtItemChanged(block: () -> Unit) {
        extViewNotifyHelper.notifyExtItemChange(block)
    }

//    override fun notifyDataRangeChange(position: Int, length: Int, block: () -> Unit) {
//        extViewNotifyHelper.notifyExtItemChange(block) {
//            notifyItemRangeChanged(position + headerStatus.moveRange.length, length)
//        }
//    }

    // endregion

    // region 工具方法
    /**
     * 根据列表中的位置获取数据中的位置
     */
    open fun getDataPosition(viewPosition: Int): Int {
        var dataPosition = -1
        if (isDataPosition(viewPosition)) {
            dataPosition = viewPosition - getHeaderNum()
        }
        return dataPosition
    }

    /**
     * 获取数据长度
     */
    open fun getDataItemCount(): Int {
        return length
    }

    protected open fun getEFItemViewType(position: Int): Int {
        return 0
    }

    protected open fun getHeaderNum(): Int {
        return if (isHaveHead()) 1 else 0
    }

    protected open fun getFooterNum(): Int {
        return if (isHaveFoot()) 1 else 0
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
        return isHaveFoot() && getDataItemCount() == adapterPosition
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
        return headerViewBinding != null
    }

    /**
     * 是否有头布局
     */
    protected open fun isHaveFoot(): Boolean {
        return footerViewBinding != null
    }

    /**
     * 是否有空布局
     */
    protected open fun isHaveEmpty(): Boolean {
        return emptyViewBinding != null
    }

    open fun getHeaderViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): HeaderViewHolder<*>? {
        return headerViewBinding?.let {
            HeaderViewHolder(it)
        }
    }

    open fun getFooterViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): FooterViewHolder<*>? {
        return footerViewBinding?.let {
            FooterViewHolder(it)
        }
    }

    open fun getEmptyViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): EmptyViewHolder<ViewBinding>? {
        return emptyViewBinding?.let {
            EmptyViewHolder(it)
        }
    }

    fun addHeaderViewBinding(
        recyclerView: RecyclerView,
        bind: (LayoutInflater, ViewGroup, Boolean) -> ViewBinding
    ) {
        headerViewBinding = bind(LayoutInflater.from(recyclerView.context), recyclerView, false)
    }

    fun addEmptyViewBinding(
        recyclerView: RecyclerView,
        bind: (LayoutInflater, ViewGroup, Boolean) -> ViewBinding
    ) {
        emptyViewBinding = bind(LayoutInflater.from(recyclerView.context), recyclerView, false)
    }

    fun addFooterViewBinding(
        recyclerView: RecyclerView,
        bind: (LayoutInflater, ViewGroup, Boolean) -> ViewBinding
    ) {
        footerViewBinding = bind(LayoutInflater.from(recyclerView.context), recyclerView, false)
    }

    inline fun <reified T> buildHeaderBinding(binding: T.() -> Unit){
        (headerViewBinding as? T)?.let {
            binding(it)
        }
    }

    //扩展ViewHolder需要继承的布局
    open inner class ExtViewHolder<VB : ViewBinding>(binding: VB) :
        BaseVBRecyclerViewHolder<Any?, VB>(binding) {
        override fun onBindViewHolder(data: Any?, position: Int) {}
    }

    inner class EmptyViewHolder<VB : ViewBinding>(binding: VB) : ExtViewHolder<VB>(binding)

    inner class FooterViewHolder<VB : ViewBinding>(binding: VB) : ExtViewHolder<VB>(binding)

    inner class HeaderViewHolder<VB : ViewBinding>(binding: VB) : ExtViewHolder<VB>(binding)
    // endregion

    /**
     * 额外布局刷新
     * 在数据变化前后记录额外布局变化。
     * 根据变化在数据item刷新前更新空布局和头布局，之后更新足布局。
     * 只改变增删，change需要手动更新
     */
    inner class ExtViewNotifyHelper {
        var emptyStatus: RangeStatus = EmptyRangeStatus()
            private set
        var headerStatus: RangeStatus = HeaderRangeStatus()
            private set
        var footerStatus: RangeStatus = FooterRangeStatus()
            private set

        fun notifyExtItemChange(
            block: () -> Unit,
            notifyDataChange: ExtViewNotifyHelper.() -> Unit = {}
        ) {
            // 记录，更新数据
            beforeDataChange()
            block()
            afterDataChange()
            // 更新视图
            beforeItemChange()
            notifyDataChange(this)
            afterItemChange()
        }

        // 记录数据变化之前
        private fun beforeDataChange() {
            emptyStatus.recordOld()
            headerStatus.recordOld()
            footerStatus.recordOld()
        }

        // 记录数据变化之后
        private fun afterDataChange() {
            emptyStatus.recordNew()
            headerStatus.recordNew()
            footerStatus.recordNew()
        }

        private fun beforeItemChange() {
            headerStatus.notifyItemChanged()
            emptyStatus.notifyItemChanged()
        }

        private fun afterItemChange() {
            footerStatus.notifyItemChanged()
        }

        abstract inner class RangeStatus {
            private val oldRange by lazy {
                Range()
            }

            private val newRange by lazy {
                Range()
            }

            val moveRange by lazy {
                Range()
            }

            abstract fun recordRange(range: Range)

            fun recordOld() {
                recordRange(oldRange)
            }

            fun recordNew() {
                recordRange(newRange)
                mergeRange()
            }

            // 刷新布局
            fun notifyItemChanged() {
                with(moveRange) {
                    if (length > 0) {
                        notifyItemRangeInserted(start, length)
                    } else if (length < 0) {
                        notifyItemRangeRemoved(start, -length)
                    }
                }
            }

            // 合并变化前后的range
            private fun mergeRange() {
                val unChangeLength = newRange.length.coerceAtMost(oldRange.length)
                val start = newRange.start + unChangeLength
                val moveLength = newRange.length - oldRange.length
                moveRange.start = start
                moveRange.length = moveLength
            }
        }

        inner class EmptyRangeStatus() : RangeStatus() {
            override fun recordRange(range: Range) {
                range.start = getHeaderNum()
                range.length = getEmptyNum()
            }
        }

        inner class HeaderRangeStatus() : RangeStatus() {
            override fun recordRange(range: Range) {
                range.start = 0
                range.length = getHeaderNum()
            }
        }

        inner class FooterRangeStatus() : RangeStatus() {
            override fun recordRange(range: Range) {
                range.start = getHeaderNum() + getEmptyNum() + getDataItemCount()
                range.length = getFooterNum()
            }
        }

        inner class Range(var start: Int = 0, var length: Int = 0)
    }
}