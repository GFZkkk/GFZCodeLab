package com.gfz.mvp.adapter

import android.util.SparseBooleanArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gfz.mvp.R
import com.gfz.mvp.base.adapter.BaseMultipleChooseAdapter
import com.gfz.mvp.base.adapter.BaseRecyclerViewHolder
import com.gfz.mvp.model.bean.MultipleChooseBean
import kotlinx.android.synthetic.main.item_multiple_choose.view.*

/**
 * created by gaofengze on 2020/4/14
 */

class TestMutilChooseAdapter(dataList: List<MultipleChooseBean?> = ArrayList()) :
    BaseMultipleChooseAdapter<MultipleChooseBean>(dataList, R.layout.item_multiple_choose) {
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
        view: View,
        viewType: Int
    ): BaseRecyclerViewHolder<MultipleChooseBean> {
        return ViewHolder(view)
    }



    /**
     * 是否是新的一组
     */
    fun isNewGroup(position: Int): Boolean = if (position == 0) true else getData(position)?.groupId != getData(position - 1)?.groupId

    inner class ViewHolder(itemView: View) : BaseRecyclerViewHolder<MultipleChooseBean>(itemView) {

        override fun onBindViewHolder(data: MultipleChooseBean, position: Int) {
            val ivChoose: ImageView = itemView.iv_choose

            val tvTitle: TextView = itemView.tv_title

            val clTitleChoose: ImageView = itemView.cl_title_choose

            itemView.cl_title.setDisplay(isNewGroup(position))
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