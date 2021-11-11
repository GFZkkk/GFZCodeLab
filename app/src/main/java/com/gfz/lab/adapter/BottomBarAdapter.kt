package com.gfz.lab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gfz.recyclerview.adapter.BaseRecyclerViewAdapter
import com.gfz.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.recyclerview.adapter.BaseVBRecyclerViewHolder
import com.gfz.lab.databinding.ItemBottomBarBinding
import com.gfz.lab.model.bean.TabItemBean

/**
 * 底部导航栏item
 * created by gaofengze on 2021/5/13
 */
class BottomBarAdapter : BaseRecyclerViewAdapter<TabItemBean>() {

    init {
        needAutoRefreshClickItem = true
    }

    override fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<TabItemBean> {
        return ViewHolder(ItemBottomBarBinding.inflate(layoutInflater, parent, false))
    }

    inner class ViewHolder(binding: ItemBottomBarBinding): BaseVBRecyclerViewHolder<TabItemBean, ItemBottomBarBinding>(binding){

        override fun onBindViewHolder(data: TabItemBean, position: Int) {
            val choose = getClickIndex() == position
            with(binding){
                ivBottomIcon.setImageResource(data.getIconRes(choose))
                tvBottomDes.setText(data.desRes)
                tvBottomDes.setTextColorRes(data.getDesColorRes(choose))
            }
        }

    }


}