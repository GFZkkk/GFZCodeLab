package com.gfz.mvp.adapter

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gfz.mvp.R
import com.gfz.mvp.base.adapter.BaseRecyclerViewAdapter
import com.gfz.mvp.base.adapter.BaseRecyclerViewHolder
import com.gfz.mvp.model.bean.MoveBean
import com.gfz.mvp.utils.TopLog
import kotlinx.android.synthetic.main.item_move.view.*
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
        setLayoutId(R.layout.item_move)
    }

    override fun getViewHolder(view: View, viewType: Int): BaseRecyclerViewHolder<MoveBean> {
        return ViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        helper?.attachToRecyclerView(recyclerView)
    }

    fun setCanMove(canMove: Boolean){
        callBack?.setCanMove(canMove)
    }

    inner class ViewHolder(view: View) : BaseRecyclerViewHolder<MoveBean>(view) {

        override fun onBindViewHolder(data: MoveBean, position: Int) {
            itemView.tv_name.text = data.name

            itemView.iv_move.setOnLongClickListener {
                setCanMove(true)
                true
            }
            itemView.iv_move.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_UP){
                    setCanMove(false)
                }
                false
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