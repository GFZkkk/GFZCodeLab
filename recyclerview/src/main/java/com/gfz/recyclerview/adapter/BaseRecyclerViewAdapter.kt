package com.gfz.recyclerview.adapter

import android.os.SystemClock
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.gfz.common.utils.TimeCell


/**
 * RecyclerView的adapter的基类
 * created by gaofengze on 2020-01-19
 */

abstract class BaseRecyclerViewAdapter<T>(dataList: List<T?> = ArrayList()) :
    RecyclerView.Adapter<BaseRecyclerViewHolder<T>>() {

    /**
     * 主要数据
     */
    private val list: MutableList<T?> = ArrayList()

    /**
     * 获取数据长度
     */
    val length
        get() = getDataList().size

    /**
     * 当前点击的position
     */
    private var clickIndex: Int = -1

    /**
     * 点击事件
     */
    private var listener: ((View, Int) -> Unit)? = null

    /**
     * 带数据的点击事件
     */
    private var dataListener: ((View, Int, T) -> Unit)? = null

    /**
     * 是否自动刷新点击的item
     */
    var needAutoRefreshClickItem = false

    /**
     * 是否自动设置当前点击的position为clickIndex
     */
    var needAutoSetClickIndex = true

    /**
     * 是否自动过滤空数据
     */
    var needAutoFilterEmptyData = true

    val timeCell: TimeCell by lazy {
        TimeCell()
    }

    init {
        addAllData(getPreData() ?: dataList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<T> {
        val holder = onCreateViewHolder(LayoutInflater.from(parent.context), parent, viewType)
        holder.setHolderListener(::clickEvent)
        return holder
    }

    /**
     * 给holder提供数据
     * 绑定点击事件
     */
    override fun onBindViewHolder(@NonNull holder: BaseRecyclerViewHolder<T>, position: Int) {
        holder.bindViewHolder(getData(position), position)
    }

    /**
     * 抽象方法得到子类viewHolder
     */
    abstract fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<T>

    /**
     * 列表长度
     */
    override fun getItemCount(): Int = length

    /**
     * 得到当前点击的itemIndex
     */
    fun getClickIndex(): Int = clickIndex

    /**
     * 主动设置选中的itemIndex
     */
    open fun setClickIndex(clickIndex: Int) {
        val preClickIndex = this.clickIndex
        this.clickIndex = clickIndex
        if (needAutoRefreshClickItem && preClickIndex != clickIndex) {
            notifyDataRangeChanged(preClickIndex)
            notifyDataRangeChanged(clickIndex)
        }
    }

    // region 点击事件

    fun setOnItemClickListener(listener: ((View, Int) -> Unit)?) {
        this.listener = listener
    }

    fun setOnItemClickDataListener(dataListener: ((View, Int, T) -> Unit)?) {
        this.dataListener = dataListener
    }

    /**
     * 处理内部点击事件
     * 可用于处理点击去重
     * @return 是否消费掉了此次点击事件
     */
    open fun click(v: View?, position: Int): Boolean = false

    /**
     * 设置点击事件
     * @param v 点击的视图
     */
    open fun clickEvent(v: View, position: Int) {
        // item范围
        if (!isItemIndex(position)) return
        // 快速点击
        if (fastClick()) return
        // 内部消耗
        if (click(v, position)) return
        // 更新选中下标
        if (needAutoSetClickIndex) {
            setClickIndex(position)
        }
        // view点击事件
        listener?.invoke(v, position)
        // 数据点击事件
        getDataByPosition(position)?.let {
            dataListener?.invoke(v, position, it)
        }

    }
    // endregion

    // region 数据

    /**
     * 适用于数据固定的情况
     */
    open fun getPreData(): MutableList<T?>? = null

    /**
     * @return 绑定的数据集合
     */
    fun getDataList(): List<T?> = list

    /**
     * @return 绑定的某个位置的数据
     */
    open fun getData(position: Int): T? = if (isDataIndex(position)) list[position] else null

    // region 修改并刷新
    /**
     * 刷新全部数据
     */
    open fun refresh(data: List<T?>?) {
        val oldLength = length
        val newLength = data?.size ?: 0
        setDataList(data)
        notifyDataRangeChanged(0, oldLength.coerceAtMost(newLength))
        if (oldLength > newLength){
            notifyDataRangeRemoved(newLength, oldLength - newLength)
        } else if(oldLength < newLength){
            notifyDataRangeInserted(oldLength, newLength - oldLength)
        }
    }

    /**
     * 刷新添加数据列表后的视图
     */
    open fun addAll(data: List<T?>) {
        addAllData(data)
        notifyDataRangeInserted(itemCount - data.size, data.size)
    }

    /**
     * 刷新添加某个数据后的视图
     */
    open fun add(data: T) {
        addData(data)
        notifyDataRangeInserted(itemCount)
    }

    /**
     * 刷新某个数据
     */
    open fun replace(position: Int, data: T) {
        setData(position, data)
        notifyDataRangeChanged(position)
    }

    /**
     * 刷新移除某个位置的数据后的视图
     */
    open fun remove(position: Int) {
        removeData(position)
        notifyDataRangeRemoved(position)
    }
    // endregion

    // region 修改数据但不刷新界面
    /**
     * 设置list
     */
    fun setDataList(data: List<T?>?) {
        clear()
        addAllData(data)
    }

    /**
     * 添加数据列表
     */
    fun addAllData(dataList: List<T?>?) {
        if (dataList != null) {
            if (needAutoFilterEmptyData) {
                for (data in dataList) {
                    addData(data)
                }
            } else {
                list.addAll(dataList)
            }
        }
    }

    /**
     * 添加单个数据
     */
    fun addData(data: T?) {
        if (needAutoFilterEmptyData && data == null) {
            return
        }
        list.add(data)
    }

    /**
     * 设置某个位置的数据
     */
    fun setData(position: Int, data: T?) {
        if (isDataIndex(position)) {
            if (data == null && needAutoFilterEmptyData) {
                removeData(position)
            } else {
                list[position] = data
            }
        }
    }

    /**
     * 移除某个位置的数据
     */
    fun removeData(position: Int) {
        if (isDataIndex(position)) {
            list.removeAt(position)
        }
    }

    /**
     * 清空数据
     */
    open fun clear() {
        list.clear()
    }
    // endregion

    // region 判断方法
    /**
     * 是否是数组下标
     */
    open fun isDataIndex(position: Int): Boolean = position in 0 until length

    /**
     * 是否是item下标
     */
    open fun isItemIndex(position: Int): Boolean = position in 0 until itemCount

    open fun isFirstData(position: Int) = position == 0

    open fun isLastData(position: Int) = position == length - 1

    /**
     * 根据item的位置获取数据
     * @param holderPosition
     * @return
     */
    protected open fun getDataByPosition(holderPosition: Int): T? {
        return getData(holderPosition)
    }
    // endregion

    // endregion

    // region 刷新

    /**
     * 刷新改变item的位置
     */
    open fun notifyDataRangeInserted(position: Int, length: Int = 1) {
        notifyItemRangeInserted(position, length)
    }

    /**
     * 刷新改变item的位置
     */
    open fun notifyDataRangeChanged(position: Int, length: Int = 1) {
        if (isItemIndex(position)) {
            notifyItemRangeChanged(position, length)
        }
    }

    /**
     * 刷新删除item的位置
     */
    open fun notifyDataRangeRemoved(position: Int, length: Int = 1) {
        if (isItemIndex(position)) {
            notifyItemRangeRemoved(position, length)
        }
    }

    // endregion

    // region 工具方法
    /**
     * item点击间隔
     */
    open fun getClickDoubleTime(): Int = 500

    /**
     * 防止重复点击
     */
    private fun fastClick(): Boolean {
        return timeCell.fastClick(0, 500)
    }
}
