package com.gfz.lab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gfz.lab.databinding.ItemTestBinding
import com.gfz.recyclerview.adapter.BaseRecyclerViewAdapter
import com.gfz.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.recyclerview.adapter.BaseVBRecyclerViewHolder

/**
 *
 * created by xueya on 2022/8/2
 */
class TestSlideDeleteAdapter : BaseRecyclerViewAdapter<String>() {

    private val callback by lazy {
        SlideDeleteCallback(this)
    }

    private val itemTouchHelper by lazy {
        ItemTouchHelper(callback)
    }

    override fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<String> {
        return ViewHolder(
            ItemTestBinding.inflate(
                layoutInflater, parent, false
            )
        )
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun getPreData(): MutableList<String?> {
        return mutableListOf(
            "测试数据1",
            "测试数据2",
            "测试数据3",
            "测试数据4",
            "测试数据5",
        )
    }

    inner class ViewHolder(binding: ItemTestBinding) :
        BaseVBRecyclerViewHolder<String, ItemTestBinding>(binding) {
        override fun onBindViewHolder(data: String, position: Int) {
            bindView {
                tvTitle.text = data
            }
        }

    }

    class SlideDeleteCallback<T>(var adapter: BaseRecyclerViewAdapter<T>) : ItemTouchHelper.Callback(){
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(0, ItemTouchHelper.START)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            TODO("Not yet implemented")
        }

    }
}