package com.gfz.mvp.base.recyclerview.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * #BaseRecyclerViewAdapter
 * created by gaofengze on 2020-01-19
 */

abstract class BaseRecyclerViewHolder<T>(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var context: Context? = null

    init {
        context = binding.root.context
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

    // region 工具方法
    fun TextView.setTextColorRes(@ColorRes color: Int){
        this.setTextColor(ContextCompat.getColor(context, color))
    }
    // endregion

    // region 判断方法
    // endregion
}