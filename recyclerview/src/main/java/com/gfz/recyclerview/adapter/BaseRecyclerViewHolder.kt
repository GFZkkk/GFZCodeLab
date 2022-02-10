package com.gfz.recyclerview.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


/**
 * #BaseRecyclerViewAdapter
 * created by gaofengze on 2020-01-19
 */

abstract class BaseRecyclerViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val context: Context
        get() = itemView.context

    private var listener: ((View, Int) -> Unit)? = null

    init {
        initEvent()
    }

    // region 加载方法
    open fun initEvent() {}

    fun bindViewHolder(data: T?, position: Int) {
        if (data != null) {
            onBindViewHolder(data, position)
        } else {
            bindNoDataViewHolder()
        }
    }

    protected abstract fun onBindViewHolder(data: T, position: Int)

    protected open fun bindNoDataViewHolder() {}
    // endregion

    // region 对外方法
    open fun getHolderPosition(): Int {
        return bindingAdapterPosition
    }

    open fun setHolderListener(listener: ((View, Int) -> Unit)?) {
        this.listener = listener
    }
    // endregion

    // region 工具方法
    protected fun setViewOnClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener { v ->
                click(v)
            }
        }
    }

    //将点击事件传出去
    protected fun click(v: View) {
        listener?.let {
            val position = getHolderPosition()
            if (position == -1) {
                return
            }
            it.invoke(v, position)
        }
    }

    fun TextView.setTextColorRes(@ColorRes color: Int) {
        this.setTextColor(ContextCompat.getColor(context, color))
    }
    // endregion

    // region 判断方法
    // endregion
}