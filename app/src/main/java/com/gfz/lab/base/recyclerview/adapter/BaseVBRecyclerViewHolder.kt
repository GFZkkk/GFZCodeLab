package com.gfz.lab.base.recyclerview.adapter
import androidx.viewbinding.ViewBinding

abstract class BaseVBRecyclerViewHolder<T, VB : ViewBinding>(val binding: VB) : BaseRecyclerViewHolder<T>(binding.root) {
    
}