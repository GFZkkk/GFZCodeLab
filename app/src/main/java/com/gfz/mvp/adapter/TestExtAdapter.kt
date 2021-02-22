package com.gfz.mvp.adapter

import android.view.View
import android.view.ViewGroup
import com.gfz.mvp.base.adapter.BaseExtLayoutAdapter
import com.gfz.mvp.base.adapter.BaseRecyclerViewAdapter
import com.gfz.mvp.base.adapter.BaseRecyclerViewHolder
import com.gfz.mvp.databinding.ItemExtDataBinding
import com.gfz.mvp.utils.viewBind

/**
 * 测试扩展布局
 * created by gaofengze on 2021/2/22
 */
class TestExtAdapter : BaseExtLayoutAdapter<Number>() {

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<Number> {
        return ViewHolder(viewBind(parent))
    }

    inner class ViewHolder(private val binding: ItemExtDataBinding) : DataViewHolder<Number>(binding){
        override fun onBindViewHolder(data: Number, position: Int) {
            binding.apply {
                tvTest.text = data.toString()
            }
        }

    }



}