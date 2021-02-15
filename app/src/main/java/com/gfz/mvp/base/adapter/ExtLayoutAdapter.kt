package com.gfz.mvp.base.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 *
 * created by gaofengze on 2021/1/27
 */
abstract class ExtLayoutAdapter<T>(
    list: List<T> = ArrayList()
) : BaseRecyclerViewAdapter<T?>(list) {

    /*protected val EMPTY = -1
    protected val FOOT = -2
    protected val HEAD = -3
    private var footerView: View? = null
    private var emptyView: View? = null
    private var headerView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<T?> {
        val holder = onCreateViewHolder(getViewByViewType(parent, viewType), viewType)
        if (isDataViewType(viewType)) {
            setHolderListener(holder)
        }
        return holder
    }

    fun onCreateViewHolder(view: View?, viewType: Int): BaseRecyclerViewHolder<T?> {
        return when (viewType) {
            EMPTY -> getEmptyViewHolder(view)
            FOOT -> getFooterViewHolder(view)
            HEAD -> getHeaderViewHolder(view)
            else -> onCreateDataViewHolder(view, viewType)
        }
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<T?>, position: Int) {
        position = getDataPosition(position)
        holder.onBindViewHolder(getData(position), position)
    }

    protected fun getViewByViewType(viewGroup: ViewGroup?, viewType: Int): View {
        return when (viewType) {
            HEAD -> {
                if (headerView != null) {
                    return headerView
                }
                if (footerView != null) {
                    return footerView
                }
                if (emptyView != null) {
                    emptyView
                } else super.getViewByViewType(viewGroup, viewType)
            }
            FOOT -> {
                if (footerView != null) {
                    return footerView
                }
                if (emptyView != null) {
                    emptyView
                } else super.getViewByViewType(viewGroup, viewType)
            }
            EMPTY -> {
                if (emptyView != null) {
                    emptyView
                } else super.getViewByViewType(viewGroup, viewType)
            }
            else -> super.getViewByViewType(viewGroup, viewType)
        }
    }

    abstract fun onCreateDataViewHolder(view: View?, viewType: Int): DataViewHolder<T>?

    override fun getItemCount(): Int {
        if (isHaveEmpty() && getDataItemCount() == 0) return 1
        return if (isHaveFoot() || isHaveHead()) getDataItemCount() + 1 else getDataItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        if (isEmptyView()) return EMPTY
        if (isHeadView(position)) return HEAD
        return if (isFootView(position)) FOOT else getEFItemViewType(getDataPosition(position))
    }

    *//**
     * 根据列表中的位置获取数据中的位置
     * 如果是扩展类型则不做处理
     *//*
    fun getDataPosition(position: Int): Int {
        var position = position
        if (isDataPosition(position)) {
            if (isHaveHead()) {
                position--
            }
        }
        return position
    }

    fun setFooterView(view: View?) {
        footerView = view
    }

    fun setEmptyView(emptyView: View?) {
        this.emptyView = emptyView
    }

    fun setHeaderView(headerView: View?) {
        this.headerView = headerView
    }

    fun notifyAllDataChanged() {
        if (isHaveHead()) {
            notifyItemRangeChanged(1, getDataItemCount())
        } else {
            notifyItemRangeChanged(0, getDataItemCount())
        }
    }

    fun notifyDataChanged(dataPosition: Int) {
        if (isHaveHead()) {
            notifyChanged(dataPosition + 1)
        } else {
            notifyChanged(dataPosition)
        }
    }

    *//**
     * 获取数据长度
     *//*
    fun getDataItemCount(): Int {
        return getData().size()
    }

    protected fun getEFItemViewType(position: Int): Int {
        return 0
    }

    protected fun getEmptyLayoutId(): Int {
        return 0
    }

    protected fun getFooterLayoutId(): Int {
        return 0
    }

    protected fun getHeaderLayoutId(): Int {
        return 0
    }

    *//**
     * 是否是数据的类型
     *//*
    protected fun isDataViewType(viewType: Int): Boolean {
        return viewType >= 0
    }

    *//**
     * 是否是数据的位置
     *//*
    protected fun isDataPosition(adapterPosition: Int): Boolean {
        return !isExtView(adapterPosition)
    }

    *//**
     * 是否有足布局
     *//*
    protected fun isHaveHead(): Boolean {
        return getHeaderLayoutId() != 0 || headerView != null
    }

    *//**
     * 是否有头布局
     *//*
    protected fun isHaveFoot(): Boolean {
        return getFooterLayoutId() != 0 || footerView != null
    }

    *//**
     * 是否有空布局
     *//*
    private fun isHaveEmpty(): Boolean {
        return getEmptyLayoutId() != 0 || emptyView != null
    }

    *//**
     * 是否是空布局
     *//*
    protected fun isEmptyView(): Boolean {
        return isHaveEmpty() && getDataItemCount() == 0
    }

    *//**
     * 是否是足布局
     *//*
    protected fun isFootView(adapterPosition: Int): Boolean {
        return isHaveFoot() && getDataItemCount() == adapterPosition
    }

    *//**
     * 是否是头布局
     *//*
    protected fun isHeadView(adapterPosition: Int): Boolean {
        return isHaveHead() && adapterPosition == 0
    }

    protected fun isExtView(adapterPosition: Int): Boolean {
        return isHeadView(adapterPosition) || isFootView(adapterPosition) || isEmptyView()
    }

    fun getHeaderViewHolder(view: View?): HeaderViewHolder {
        return HeaderViewHolder(view)
    }

    fun getFooterViewHolder(view: View?): FooterViewHolder {
        return FooterViewHolder(view)
    }

    fun getEmptyViewHolder(view: View?): EmptyViewHolder {
        return EmptyViewHolder(view)
    }

    *//**
     * 如果是GridLayoutManager布局，空布局需要独占一行
     * 重写该方法时注意处理
     *//*
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            val gridManager = manager
            gridManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isEmptyView() || isFootView(position)) {
                        1
                    } else gridManager.spanCount
                }
            }
        }
    }

    //针对流式布局
    override fun onViewAttachedToWindow(holder: BaseRecyclerViewHolder) {
        val layoutPosition: Int = holder.getLayoutPosition()
        if (isEmptyView() || isFootView(layoutPosition)) {
            val layoutParams: ViewGroup.LayoutParams = holder.itemView.getLayoutParams()
            if (layoutParams != null) {
                if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                    //占领全部空间;
                    layoutParams.isFullSpan = true
                }
            }
        }
    }

    //数据viewholder需要继承的布局
    abstract inner class DataViewHolder<T>(itemView: View?) :
        BaseRecyclerViewHolder<T>(itemView) {
        val holderPosition: Int
            get() = getDataPosition(layoutPosition)
    }

    //扩展ViewHolder需要继承的布局
    class ExtViewHolder(itemView: View?) : BaseRecyclerViewHolder(itemView) {
        fun onBindViewHolder(data: Any?, position: Int) {}
    }

    class EmptyViewHolder(itemView: View?) : ExtViewHolder(itemView) {
        override fun onBindViewHolder(data: Any?, position: Int) {}
    }

    class FooterViewHolder(itemView: View?) : ExtViewHolder(itemView) {
        override fun onBindViewHolder(data: Any?, position: Int) {}
    }

    class HeaderViewHolder(itemView: View?) : ExtViewHolder(itemView) {
        override fun onBindViewHolder(data: Any?, position: Int) {}
    }

    init {
        addItemType(EMPTY, getEmptyLayoutId())
        addItemType(FOOT, getFooterLayoutId())
    }*/
}