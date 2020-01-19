package com.gfz.mvp.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * created by gaofengze on 2020-01-19
 */

abstract class BaseRecyclerViewHolder<T>(view : View) : RecyclerView.ViewHolder(view) {
    abstract fun onBindViewHolder(data : T?, position : Int)
}