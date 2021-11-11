package com.gfz.lab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gfz.recyclerview.adapter.BaseRecyclerViewAdapter
import com.gfz.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.recyclerview.adapter.BaseVBRecyclerViewHolder
import com.gfz.lab.databinding.ItemTestBinding
import com.gfz.lab.model.bean.TestBean

class Test3Adapter(list: MutableList<TestBean?> = ArrayList()) :
    BaseRecyclerViewAdapter<TestBean>(list) {

    override fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<TestBean> {
        return ViewHolder(ItemTestBinding.inflate(layoutInflater, parent, false))
    }

    class ViewHolder(binding: ItemTestBinding): BaseVBRecyclerViewHolder<TestBean, ItemTestBinding>(binding){
        override fun onBindViewHolder(data: TestBean, position: Int) {
            binding.tvTitle.text = data.str
        }
    }

}