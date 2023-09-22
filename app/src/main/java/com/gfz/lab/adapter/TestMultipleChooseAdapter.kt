package com.gfz.lab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfz.lab.R
import com.gfz.recyclerview.adapter.BaseMultipleChooseAdapter
import com.gfz.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.recyclerview.adapter.BaseVBRecyclerViewHolder
import com.gfz.lab.databinding.ItemMultipleChooseBinding
import com.gfz.common.ext.setDisplay
import com.gfz.lab.model.bean.MultipleChooseBean

/**
 * created by gaofengze on 2020/4/14
 */

class TestMultipleChooseAdapter(dataList: List<MultipleChooseBean?> = ArrayList()) :
    BaseMultipleChooseAdapter<MultipleChooseBean>(dataList) {
    var groupId = -1

    init {
        setViewBound(10)
    }

    override fun getCheckBoxIdByPosition(position: Int): Int {
        return R.id.iv_choose
    }

    override fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<MultipleChooseBean> {
        return ViewHolder(ItemMultipleChooseBinding.inflate(layoutInflater,parent,false))
    }

    inner class ViewHolder(binding: ItemMultipleChooseBinding) : BaseVBRecyclerViewHolder<MultipleChooseBean, ItemMultipleChooseBinding>(binding) {

        override fun initEvent() {
            super.initEvent()
            binding.ivChoose.addClickListener()
        }

        override fun onBindViewHolder(data: MultipleChooseBean, position: Int) {
            with(binding){
                clTitle.setDisplay(isNewGroup(position))
                if (isMultipleChooseItem(position)) {
                    clTitleChoose.setImageResource(R.drawable.study_change_color_choose)
                } else {
                    clTitleChoose.setImageResource(R.drawable.study_change_color_unchoose)
                }

                if (isChooseItem(position)) {
                    ivChoose.setImageResource(R.drawable.selected)
                } else {
                    ivChoose.setImageResource(R.drawable.unselected)
                }
                tvTitle.text = data.title
                clTitleChoose.setOnClickListener {
                    changeGroupChooseStatus(position)
                }
            }
        }
    }
}