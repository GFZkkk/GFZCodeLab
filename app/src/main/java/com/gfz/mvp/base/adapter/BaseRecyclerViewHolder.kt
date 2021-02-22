package com.gfz.mvp.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * #BaseRecyclerViewAdapter
 * created by gaofengze on 2020-01-19
 */

abstract class BaseRecyclerViewHolder<T>(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    /**
     * 空数据将不被显示
     */
    fun bindViewHolder(data: T?, position: Int) {
        if (data != null){
            onBindViewHolder(data, position)
        }else{
            bindNoDataViewHolder()
        }
    }

    /**
     * 隐藏没有数据的viewholder
     */
    open fun bindNoDataViewHolder(){
        itemView.visibility = View.INVISIBLE
    }

    open fun getHolderPosition(): Int {
        return layoutPosition
    }

    abstract fun onBindViewHolder(data: T, position: Int)
}