package com.gfz.mvp.adapter

import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gfz.mvp.base.adapter.BaseRecyclerViewAdapter
import com.gfz.mvp.base.adapter.BaseRecyclerViewHolder
import com.gfz.mvp.databinding.ItemMoveBinding
import com.gfz.mvp.model.bean.MoveBean
import com.gfz.mvp.utils.TopLog
import com.gfz.mvp.utils.viewBind
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

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<MoveBean> {
        return ViewHolder(viewBind(parent))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        helper?.attachToRecyclerView(recyclerView)
    }

    fun setCanMove(canMove: Boolean){
        callBack?.setCanMove(canMove)
    }

    inner class ViewHolder(private val binding: ItemMoveBinding) : BaseRecyclerViewHolder<MoveBean>(binding) {

        override fun onBindViewHolder(data: MoveBean, position: Int) {
            with(binding){
                tvName.text = data.name

                ivMove.setOnLongClickListener {
                    setCanMove(true)
                    true
                }
                ivMove.setOnTouchListener { v, event ->
                    if(event.action == MotionEvent.ACTION_UP){
                        setCanMove(false)
                    }
                    false
                }
            }

        }
    }

    class RecycItemTouchHelper<T>(var adapter: BaseRecyclerViewAdapter<T>) : ItemTouchHelper.Callback() {

        private var canMove: Boolean = false

        fun setCanMove(canMove: Boolean){
            this.canMove = canMove
            TopLog.e(canMove)
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = if(canMove){
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }else{
                0
            }
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            TopLog.e(dragFlags)
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            TopLog.e(canMove)
            if (!canMove){
                return false
            }
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(adapter.getData(), i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(adapter.getData(), i, i - 1)
                }
            }
            adapter.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }
    }
}