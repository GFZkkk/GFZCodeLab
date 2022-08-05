package com.gfz.recyclerview.adapter

import androidx.viewbinding.ViewBinding

abstract class BaseVBRecyclerViewHolder<T, VB : ViewBinding>(val binding: VB) :
    BaseRecyclerViewHolder<T>(binding.root) {
    protected inline fun bindView(bind: VB.() -> Unit) {
        bind(binding)
    }
}