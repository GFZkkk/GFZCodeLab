package com.gfz.mvp.adapter

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.gfz.mvp.base.recyclerview.adapter.BaseRecyclerViewAdapter
import com.gfz.mvp.base.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.mvp.databinding.ItemBottomBarBinding
import com.gfz.mvp.model.bean.TabItemBean
import com.gfz.mvp.utils.viewBind

/**
 * 底部导航栏item
 * created by gaofengze on 2021/5/13
 */
class BottomBarAdapter : BaseRecyclerViewAdapter<TabItemBean>() {

    init {
        setNeedAutoRefreshClickItem(true)
    }

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<TabItemBean> {
        return ViewHolder(viewBind(parent))
    }

    inner class ViewHolder(private val binding: ItemBottomBarBinding): BaseRecyclerViewHolder<TabItemBean>(binding){

        override fun onBindViewHolder(data: TabItemBean, position: Int) {
            val choose = getClickIndex() == position
            with(binding){
                ivBottomIcon.setImageResource(data.getIconRes(choose))
                tvBottomDes.text = data.des
                tvBottomDes.setTextColorRes(data.getDesColorRes(choose))
            }
        }

    }
}