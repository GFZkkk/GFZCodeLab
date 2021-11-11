package com.gfz.recyclerview.ext

import com.gfz.lab.ext.getCompatColor
import com.gfz.lab.ext.getCompatDrawable
import com.gfz.recyclerview.adapter.BaseRecyclerViewHolder

fun BaseRecyclerViewHolder<*>.getColor(resId: Int) = context.getCompatColor(resId)
fun BaseRecyclerViewHolder<*>.getDrawable(resId: Int) = context.getCompatDrawable(resId)