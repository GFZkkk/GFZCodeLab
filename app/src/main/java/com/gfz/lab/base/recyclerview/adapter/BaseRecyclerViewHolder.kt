package com.gfz.lab.base.recyclerview.adapter

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

    var context: Context? = null

    private var listener: ((View, Int) -> Unit)? = null

    init {
        context = itemView.context
        initEvent()
    }

    // region 加载方法
    /**
     * 空数据将不被显示
     */
    fun bindViewHolder(data: T?, position: Int) {

        if (data != null){
            itemView.visibility = View.VISIBLE
            onBindViewHolder(data, position)
        }else{
            itemView.visibility = View.INVISIBLE
            bindNoDataViewHolder()
        }
    }

    /**
     * 隐藏没有数据的viewholder
     */
    open fun bindNoDataViewHolder(){

    }

    open fun getHolderPosition(): Int {
        return layoutPosition
    }

    open fun initEvent(){}

    abstract fun onBindViewHolder(data: T, position: Int)
    // endregion

    open fun setHolderListener(listener: ((View, Int) -> Unit)?) {
        this.listener = listener
    }

    protected open fun setHolderListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener { v ->
                listener?.invoke(v, getHolderPosition())
            }
        }
    }

    //将点击事件传出去
    protected open fun click(v: View) {
        listener?.invoke(v, getHolderPosition())
    }

    // region 工具方法
    fun TextView.setTextColorRes(@ColorRes color: Int){
        this.setTextColor(ContextCompat.getColor(context, color))
    }
    // endregion

    // region 判断方法
    // endregion
}