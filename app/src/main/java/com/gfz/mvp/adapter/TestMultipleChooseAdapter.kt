package com.gfz.mvp.adapter

import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import com.gfz.mvp.R
import com.gfz.mvp.base.recyclerview.adapter.BaseMultipleChooseAdapter
import com.gfz.mvp.base.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.mvp.databinding.ItemMultipleChooseBinding
import com.gfz.mvp.model.bean.MultipleChooseBean
import com.gfz.mvp.utils.setDisplay
import com.gfz.mvp.utils.toLog
import com.gfz.mvp.utils.viewBind

/**
 * created by gaofengze on 2020/4/14
 */

class TestMultipleChooseAdapter(dataList: List<MultipleChooseBean?> = ArrayList()) :
    BaseMultipleChooseAdapter<MultipleChooseBean>(dataList) {
    var groupId = -1

    init {
        setBound(10)
    }

    override fun getChooseViewByPosition(position: Int): Int {
        return R.id.iv_choose
    }

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<MultipleChooseBean> {
        return ViewHolder(viewBind(parent))
    }

    inner class ViewHolder(private val binding: ItemMultipleChooseBinding) : BaseRecyclerViewHolder<MultipleChooseBean>(binding) {

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
                    changeGroupChooseStatus(data.groupId)
                }
                setListener(ivChoose, position)
            }
        }
    }
}