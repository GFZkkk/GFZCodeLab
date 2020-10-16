package com.gfz.mvp.adapter

import android.view.View
import com.gfz.mvp.R
import com.gfz.mvp.base.adapter.BaseRecyclerViewAdapter
import com.gfz.mvp.base.adapter.BaseRecyclerViewHolder
import com.gfz.mvp.model.bean.TestBean
import kotlinx.android.synthetic.main.item_test.view.*

class Test3Adapter(list: MutableList<TestBean?> = ArrayList()) :
    BaseRecyclerViewAdapter<TestBean>(list, R.layout.item_test) {

    override fun getViewHolder(view: View, viewType: Int): BaseRecyclerViewHolder<TestBean> {
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : BaseRecyclerViewHolder<TestBean>(view) {
        override fun onBindViewHolder(data: TestBean, position: Int) {
            itemView.tv_title.text = data.str
        }
    }
}