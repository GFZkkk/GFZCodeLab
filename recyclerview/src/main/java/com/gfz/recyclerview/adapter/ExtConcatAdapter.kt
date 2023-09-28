package com.gfz.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * 用于ConcatAdapter中的额外布局
 * 可作为多个adapter的空布局，头布局，足布局
 * 需要手动控制显隐
 * created by xueya on 2022/8/8
 */
class ExtConcatAdapter: BaseExtLayoutAdapter<Any>(){

    private var needShowEmpty = false
    private var needShowHeader = false
    private var needShowFooter = false

    fun showEmpty(show: Boolean){
        notifyExtItemChanged {
            needShowEmpty = show
        }
    }

    fun showHeader(show: Boolean){
        notifyExtItemChanged {
            needShowHeader = show
        }
    }

    fun showFooter(show: Boolean){
        notifyExtItemChanged {
            needShowFooter = show
        }
    }

    override fun onCreateDataViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<Any> {
        return UnknownViewHolder(layoutInflater, parent)
    }

    override fun isHaveHead(): Boolean {
        return super.isHaveHead() && needShowHeader
    }

    override fun isHaveFoot(): Boolean {
        return super.isHaveFoot() && needShowFooter
    }

    override fun isHaveEmpty(): Boolean {
        return super.isHaveEmpty() && needShowEmpty
    }
}