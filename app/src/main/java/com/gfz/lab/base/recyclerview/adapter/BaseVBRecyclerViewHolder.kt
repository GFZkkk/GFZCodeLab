package com.gfz.lab.base.recyclerview.adapter

import android.view.View
import androidx.viewbinding.ViewBinding

abstract class BaseVBRecyclerViewHolder<T, VB : ViewBinding>(val binding: VB) : BaseRecyclerViewHolder<T>(binding.root) {
    
}