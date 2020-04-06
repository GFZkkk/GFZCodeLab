package com.gfz.mvp.base.adapter

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EmptyAndFooterAdapter<T>(dataList: List<T?> = ArrayList(), clickIndex: Int = -1): BaseRecyclerViewAdapter<T>(dataList, clickIndex) {

    protected val EMPTY = -1
    protected val FOOT = -2

    init {
        addItemType(EMPTY, getEmptyLayoutId())
        addItemType(FOOT, getFooterLayoutId())
    }

    override fun getViewHolder(view: View, viewType: Int): BaseRecyclerViewHolder<T> {
         return when (viewType) {
             EMPTY -> getEmptyViewHolder(view)
             FOOT -> getFooterViewHolder(view)
             else -> getEFViewHolder(view, viewType)
         }
     }

    abstract fun getEFViewHolder(view: View, viewType: Int): BaseRecyclerViewHolder<T>

    override fun getItemCount(): Int {
        if (isHaveEmpty() && length == 0) return 1
        return if (isHaveFoot()) length + 1 else length
    }

    override fun getItemViewType(position: Int): Int {
        if (isHaveEmpty() && length == 0) return EMPTY
        return if (isHaveFoot() && length == position) FOOT else 0
    }

    protected open fun getEmptyLayoutId() = 0

    protected open fun getFooterLayoutId() = 0

    /**
     * 是否有足布局
     */
    protected fun isHaveFoot() = getFooterLayoutId() != 0

    /**
     * 是否有空布局
     */
    protected fun isHaveEmpty() = getEmptyLayoutId() != 0

    /**
     * 是否是空布局
     */
    protected fun isEmptyView(pos: Int) = getItemViewType(pos) == EMPTY

    /**
     * 是否是足布局
     */
    protected fun isFootView(pos: Int) = getItemViewType(pos) == FOOT

    open fun getFooterViewHolder(view: View) = FooterViewHolder(view)

    open fun getEmptyViewHolder(view: View) = EmptyViewHolder(view)

    /**
     * 如果是GridLayoutManager布局，空布局需要独占一行
     * 重写该方法时注意处理
     */
    override fun onAttachedToRecyclerView(@NonNull recyclerView: RecyclerView) {
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isEmptyView(position) || isFootView(position)) {
                        1
                    } else manager.spanCount
                }
            }
        }
    }

    override fun addAll(data: List<T?>) {
        addAllData(data)
        notifyItemRangeInserted(itemCount - data.size, data.size)
    }

    //针对流式布局
    override fun onViewAttachedToWindow(holder: BaseRecyclerViewHolder<T>) {
        val layoutPosition = holder.layoutPosition
        if (isEmptyView(layoutPosition) || isFootView(layoutPosition)) {
            val layoutParams = holder.itemView.layoutParams
            if (layoutParams != null) {
                if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                    //占领全部空间;
                    layoutParams.isFullSpan = true
                }
            }
        }
    }

    inner class EmptyViewHolder(itemView: View) : BaseRecyclerViewHolder<T>(itemView) {
        override fun onBindViewHolder(data: T?, position: Int) {}
    }

    inner class FooterViewHolder(itemView: View) : BaseRecyclerViewHolder<T>(itemView) {
        override fun onBindViewHolder(data: T?, position: Int) {}
    }
 }

