package com.gfz.recyclerview.ext

import com.gfz.common.ext.getCompatColor
import com.gfz.common.ext.getCompatDrawable
import com.gfz.recyclerview.adapter.BaseRecyclerViewHolder

fun BaseRecyclerViewHolder<*>.getColor(resId: Int) = context.getCompatColor(resId)
fun BaseRecyclerViewHolder<*>.getDrawable(resId: Int) = context.getCompatDrawable(resId)