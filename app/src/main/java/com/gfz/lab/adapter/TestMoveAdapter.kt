package com.gfz.lab.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gfz.recyclerview.adapter.BaseRecyclerViewAdapter
import com.gfz.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.recyclerview.adapter.BaseVBRecyclerViewHolder
import com.gfz.lab.databinding.ItemMoveBinding
import com.gfz.lab.model.bean.MoveBean
import com.gfz.common.utils.TopLog
import java.util.*

/**
 * Created by gaofengze on 2020/8/22.
 */
class TestMoveAdapter(data: List<MoveBean>) : BaseRecyclerViewAdapter<MoveBean>(data) {

    private var helper: ItemTouchHelper? = null
    var callBack: RecycItemTouchHelper<MoveBean>? = null


    init {
        callBack = RecycItemTouchHelper(this)
        helper = ItemTouchHelper(callBack!!)
    }

    override fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<MoveBean> {
        return ViewHolder(ItemMoveBinding.inflate(layoutInflater, parent, false))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        helper?.attachToRecyclerView(recyclerView)
    }

    inner class ViewHolder(binding: ItemMoveBinding) : BaseVBRecyclerViewHolder<MoveBean, ItemMoveBinding>(binding) {

        override fun onBindViewHolder(data: MoveBean, position: Int) {
            with(binding){
                tvName.text = data.name

                ivMove.setOnLongClickListener {
                    helper?.startDrag(this@ViewHolder)
                    true
                }
            }

        }
    }

    class RecycItemTouchHelper<T>(var adapter: BaseRecyclerViewAdapter<T>) : ItemTouchHelper.Callback() {

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = 0
            TopLog.e(dragFlags)
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.layoutPosition
            val toPosition = target.layoutPosition
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(adapter.getDataList(), i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(adapter.getDataList(), i, i - 1)
                }
            }
            adapter.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

        override fun isLongPressDragEnabled(): Boolean {
            return false
        }
    }
}