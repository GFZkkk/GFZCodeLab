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
    // 是否已经成功初始化
    var isInit = false

    // region 对外方法
    fun bindViewHolder(data: T?, position: Int) {
        if (!isInit){
            isInit = onInit()
        }
        if (data != null) {
            onBindViewHolder(data, position)
        } else {
            bindNoDataViewHolder()
        }
    }

    open fun getHolderPosition(): Int {
        return bindingAdapterPosition
    }

    open fun setHolderListener(listener: ((View, Int) -> Unit)?) {
        this.listener = listener
    }
    // endregion

    // region 内部方法
    protected open fun onInit(): Boolean{
        initEvent()
        return true
    }
    protected open fun initEvent() {
        itemView.addClickListener()
    }

    protected abstract fun onBindViewHolder(data: T, position: Int)

    protected open fun bindNoDataViewHolder() {}
    // endregion

    // region 工具方法
    protected fun addClickListeners(vararg views: View) {
        for (view in views) {
            view.addClickListener()
        }
    }

    protected fun View.addClickListener(){
        setOnClickListener { v ->
            click(v)
        }
    }

    //将点击事件传出去
    protected fun click(v: View) {
        listener?.invoke(v, getHolderPosition())
    }

    fun TextView.setTextColorRes(@ColorRes color: Int) {
        this.setTextColor(ContextCompat.getColor(context, color))
    }
    // endregion

    // region 判断方法
    // endregion
}