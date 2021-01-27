package com.gfz.mvp.adapter

import android.util.SparseBooleanArray
import android.view.ViewGroup
import com.gfz.mvp.R
import com.gfz.mvp.base.adapter.BaseMultipleChooseAdapter
import com.gfz.mvp.base.adapter.BaseRecyclerViewHolder
import com.gfz.mvp.databinding.ItemMultipleChooseBinding
import com.gfz.mvp.model.bean.MultipleChooseBean
import com.gfz.mvp.utils.setDisplay
import com.gfz.mvp.utils.viewBind

/**
 * created by gaofengze on 2020/4/14
 */

class TestMutilChooseAdapter(dataList: List<MultipleChooseBean?> = ArrayList()) :
    BaseMultipleChooseAdapter<MultipleChooseBean>(dataList) {
    var groupId = -1
    private var chooseTitleItem: SparseBooleanArray? = null

    init {
        chooseTitleItem = SparseBooleanArray()
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

    /**
     * 是否是新的一组
     */
    fun isNewGroup(position: Int): Boolean = if (position == 0) true else getData(position)?.groupId != getData(position - 1)?.groupId

    inner class ViewHolder(private val binding: ItemMultipleChooseBinding) : BaseRecyclerViewHolder<MultipleChooseBean>(binding) {

        override fun onBindViewHolder(data: MultipleChooseBean, position: Int) {
            with(binding){
                clTitle.setDisplay(isNewGroup(position))
                if (isMultipleChooseItem(position)) {
                    ivChoose.setImageResource(R.drawable.selected)
                } else {
                    ivChoose.setImageResource(R.drawable.unselected)
                }

                if (isMultipleChooseItem(position)) {
                    clTitleChoose.setImageResource(R.drawable.study_change_color_choose)
                } else {
                    clTitleChoose.setImageResource(R.drawable.study_change_color_unchoose)
                }
                tvTitle.text = data.title
                setListener(ivChoose, position)
                setListener(clTitleChoose, position)
            }
        }
    }
}