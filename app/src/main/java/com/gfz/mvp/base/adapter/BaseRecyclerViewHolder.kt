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

    abstract fun onBindViewHolder(data: T, position: Int)
}