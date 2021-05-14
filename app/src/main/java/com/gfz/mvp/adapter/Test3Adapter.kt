package com.gfz.mvp.adapter

import android.view.ViewGroup
import com.gfz.mvp.base.recyclerview.adapter.BaseRecyclerViewAdapter
import com.gfz.mvp.base.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.mvp.databinding.ItemTestBinding
import com.gfz.mvp.model.bean.TestBean
import com.gfz.mvp.utils.viewBind

class Test3Adapter(list: MutableList<TestBean?> = ArrayList()) :
    BaseRecyclerViewAdapter<TestBean>(list) {

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<TestBean> {
        return ViewHolder(viewBind(parent))
    }

    class ViewHolder(private val binding: ItemTestBinding): BaseRecyclerViewHolder<TestBean>(binding){
        override fun onBindViewHolder(data: TestBean, position: Int) {
            binding.tvTitle.text = data.str
        }
    }

}