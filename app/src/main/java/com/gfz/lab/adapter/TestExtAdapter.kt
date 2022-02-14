package com.gfz.lab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gfz.recyclerview.adapter.BaseExtLayoutAdapter
import com.gfz.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.lab.databinding.ItemExtDataBinding
import com.gfz.lab.databinding.LayoutHeaderTestBinding
import com.gfz.common.ext.toLog
import com.gfz.common.utils.TopLog

/**
 * 测试扩展布局
 * created by gaofengze on 2021/2/22
 */
class TestExtAdapter : BaseExtLayoutAdapter<Number>() {

    override fun onCreateDataViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<Number> {
        return ViewHolder(ItemExtDataBinding.inflate(layoutInflater, parent, false))
    }

    inner class ViewHolder(binding: ItemExtDataBinding) : DataViewHolder<Number, ItemExtDataBinding>(binding){

        override fun initEvent() {
            super.initEvent()
            with(binding){
                tvAdd.addClickListener()
                tvDelete.addClickListener()
            }
        }

        override fun onBindViewHolder(data: Number, position: Int) {
            binding.tvTest.text = data.toString()
            TopLog.e(position)
        }

    }


}