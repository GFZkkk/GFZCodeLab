package com.gfz.mvp.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gfz.mvp.App

/**
 * created by gaofengze on 2020-01-19
 */

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseRecyclerViewHolder<T>>() {

    private var list: MutableList<T> = ArrayList()
    private var context: Context? = null
    private var listener: OnItemClickListener? = null
    /**
     * 焦点下标
     */
    private var clickIndex: Int = -1
    /**
     * 是否自动刷新点击的item
     */
    private var needAutoRefreshClickItem: Boolean = false
    /**
     * 是否自动设置当前点击的position为clickIndex
     */
    private var needAutoSetClickIndex: Boolean = true

    fun BaseRecyclerViewAdapter(list: MutableList<T>? = null, clickIndex: Int = -1){
        this.list = list ?: getPreData()
        addAllData(this.list)
        setClickIndex(clickIndex)
    }

    /**
     * 给holder提供数据
     * 绑定点击事件
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<T>, position: Int) {
        holder.onBindViewHolder(getData(position), holder.adapterPosition)
        setListener(holder.itemView, holder.layoutPosition)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * 得到当前点击的itemIndex
     */
    fun getClickIndex(): Int {
        return clickIndex
    }

    /**
     * 主动设置选中的itemIndex
     */
    fun setClickIndex(clickIndex: Int) {
        if (!isItemIndex(clickIndex)) return
        val preClickIndex = this.clickIndex
        this.clickIndex = clickIndex
        if (needAutoRefreshClickItem && preClickIndex != clickIndex) {
            notifyItemChanged(preClickIndex)
            notifyItemChanged(clickIndex)
        }
    }

    /**
     * @return 绑定的数据
     */
    fun getData(): List<T> {
        return list
    }

    /**
     * @return 绑定的数据
     */
    fun getData(position: Int): T? {
        return if (isDataIndex(position)) {
            list[position]
        } else null
    }

    /**
     * 主动设置context
     */
    fun setContext(context: Context) {
        this.context = context
    }

    /**
     * 绑定点击事件
     * @param listener 点击事件接口
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    /**
     * 设置是否自动刷新点击的click，默认关闭
     */
    fun setNeedAutoRefreshClickItem(needAutoRefreshClickItem: Boolean) {
        this.needAutoRefreshClickItem = needAutoRefreshClickItem
    }

    /**
     * 设置是否自动设置选中的item为当前点击的item，默认开启
     */
    fun setNeedAutoSetClickIndex(needAutoSetClickIndex: Boolean) {
        this.needAutoSetClickIndex = needAutoSetClickIndex
    }

    /**
     * 设置点击事件
     * @param v 点击的视图
     */
    private fun clickEvent(v: View, position: Int) {
        if (!click(v, position)) {
            if (needAutoSetClickIndex) {
                setClickIndex(position)
            }
            listener?.onClick(v, getData(position), position)
        }
    }

    /**
     * 添加单个数据
     */
    fun addData(data: T) {
        list.add(data)
    }

    /**
     * 添加数据列表
     */
    fun addAllData(dataList: MutableList<T>) {
        list.addAll(dataList)
    }

    /**
     * 设置某个位置的数据
     */
    fun setData(position: Int, data: T?) {
        if (isDataIndex(position)) {
            if (data == null) {
                removeData(position)
            } else {
                list[position] = data
            }
        }
    }

    /**
     * 设置list
     */
    fun setDataList(data: MutableList<T>) {
        clear()
        addAllData(data)
    }

    /**
     * 移除某个位置的数据
     */
    fun removeData(position: Int) {
        if (isDataIndex(position)) {
            list.removeAt(position)
        }
    }

    fun getIndex(data: T): Int {
        return list.indexOf(data)
    }

    /**
     * 刷新添加某个数据后的视图
     */
    fun add(data: T) {
        addData(data)
        notifyItemInserted(itemCount)
    }

    /**
     * 刷新添加数据列表后的视图
     */
    fun addAll(data: MutableList<T>) {
        addAllData(data)
        notifyItemRangeInserted(itemCount - data.size, data.size)
    }

    /**
     * 刷新移除某个位置的数据后的视图
     */
    fun remove(position: Int) {
        removeData(position)
        notifyItemRemoved(position)
    }

    /**
     * 刷新全部数据
     */
    fun refresh(data: MutableList<T>) {
        setDataList(data)
        notifyDataSetChanged()
    }

    /**
     * 刷新某个数据
     */
    fun replace(position: Int, data: T) {
        setData(position, data)
        notifyItemChanged(position)
    }

    /**
     * 适用于提前加载数据的情况
     */
    open fun getPreData(): MutableList<T> = ArrayList()

    /**
     * 清空数据
     */
    fun clear() {
        list.clear()
    }

    /**
     * 获取创建viewHolder时的view
     * 顺便取一下context
     */
    fun getView(viewGroup: ViewGroup, layout: Int): View {
        if (context == null) {
            context = viewGroup.context
        }
        return LayoutInflater.from(viewGroup.context).inflate(layout, viewGroup, false)
    }

    /**
     * 获取一个可用的context
     */
    fun getContext(): Context {
        return context?: getAppContext()
    }

    /**
     * 设置控件显隐
     */
    fun setDisplay(view: View, display: Boolean) {
        view.visibility = if (display) View.VISIBLE else View.GONE
    }

    /**
     * 设置控件显隐
     */
    fun setShow(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    /**
     * 某个view是否显示
     */
    fun isDisplay(view: View): Boolean {
        return view.visibility == View.VISIBLE
    }

    /**
     * 设置item中控件的点击事件
     */
    fun setListener(view: View, position: Int) {
        if (isItemIndex(position)) {
            view.setOnClickListener { v -> clickEvent(view, position) }
        }
    }

    /**
     * 设置item中控件的点击事件
     */
    fun setHolderListener(holder: BaseRecyclerViewHolder<T>, view: View) {
        view.setOnClickListener { v -> clickEvent(view, holder.getLayoutPosition()) }
    }

    /**
     * 是否是数组下标
     */
    fun isDataIndex(position: Int): Boolean {
        return position > -1 && position < list.size
    }

    /**
     * 是否是item下标
     */
    fun isItemIndex(position: Int): Boolean {
        return position > -1 && position < itemCount
    }

    /**
     * item点击间隔
     */
    open fun getClickDoubleTime(): Int {
        return 300
    }

    /**
     * 拿一个全局context用来加载资源
     */
    private fun getAppContext(): Context {
        return App.appContext
    }

    /**
     * 根据资源id获取颜色
     */
    fun getColor(resId: Int): Int {
        return ContextCompat.getColor(getContext(), resId)
    }

    /**
     * 根据资源id获取图片
     */
    fun getDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(getContext(), resId)
    }

    /**
     * 根据资源id获取图片
     */
    protected fun getDrawableWithBounds(resId: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(getContext(), resId)
        drawable?.let {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        }
        return drawable
    }

    /**
     * 处理内部点击事件
     * 可用于处理点击去重
     * @return 是否消费掉了此次点击事件
     */
    open fun click(v: View, position: Int): Boolean {
        return false
    }

    interface OnItemClickListener {
        fun onClick(v: View, data: Any?, position: Int)
    }


}