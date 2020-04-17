package com.gfz.mvp.base.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gfz.mvp.data.App


/**
 * RecyclerView的adapter的基类
 * created by gaofengze on 2020-01-19
 */

abstract class BaseRecyclerViewAdapter<T>(dataList: List<T?> = ArrayList(), clickIndex: Int = -1) :
    RecyclerView.Adapter<BaseRecyclerViewHolder<T>>() {

    /**
     * 主要数据
     */
    private val list: MutableList<T?> = ArrayList()
    /**
     * 获取数据长度
     */
    val length
        get() = list.size
    /**
     * 当前点击的position
     */
    private var clickIndex: Int = -1
    /**
     * 持有的context
     */
    private var context: Context? = null
    /**
     * 点击事件
     */
    private var listener: ((View,Int) -> Unit)? = null
    /**
     * 是否自动刷新点击的item
     */
    private var needAutoRefreshClickItem = false
    /**
     * 是否自动设置当前点击的position为clickIndex
     */
    private var needAutoSetClickIndex = true
    /**
     * 是否自动过滤空数据
     */
    private var needAutoFilterEmptyData = true
    /**
     * 存储ViewType和ViewLayout的关系
     */
    private val viewHolderLayoutIds: SparseArray<Int> = SparseArray(10)

    init {
        addAllData(dataList)
        setClickIndex(clickIndex)
    }

    constructor(dataList: List<T?> = ArrayList(),
                clickIndex: Int = -1,
                layoutId: Int
    ) : this(dataList, clickIndex){
        setLayoutId(layoutId)
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<T> {
        val holder = getViewHolder(getViewByViewType(parent, viewType), viewType)
        setHolderListener(holder)
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
    abstract fun getViewHolder(view: View, viewType: Int): BaseRecyclerViewHolder<T>

    /**
     * 添加view类型以及对应的视图id
     * @param type 视图类型
     * @param layoutId 视图布局id
     */
    protected fun addItemType(type: Int, layoutId: Int) {
        viewHolderLayoutIds.append(type, layoutId)
    }

    /**
     * 设置单布局样式
     */
    protected fun setLayoutId(layoutId: Int) {
        viewHolderLayoutIds.append(0, layoutId)
    }

    /**
     * 获取多布局某个type的布局layoutId
     */
    protected fun getLayoutId(type: Int): Int = viewHolderLayoutIds.get(type, -1)
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
    protected open fun setClickIndex(clickIndex: Int) {
        if (!isItemIndex(clickIndex)) return
        val preClickIndex = this.clickIndex
        this.clickIndex = clickIndex
        if (needAutoRefreshClickItem && preClickIndex != clickIndex) {
            notifyItemChanged(preClickIndex)
            notifyItemChanged(clickIndex)
        }
    }

    /**
     * @return 绑定的数据集合
     */
    fun getData(): List<T?>? = list

    /**
     * @return 绑定的某个位置的数据
     */
    fun getData(position: Int): T? = if (isDataIndex(position)) list[position] else null

    /**
     * 主动设置context
     */
    fun setContext(context: Context?) {
        this.context = context
    }

    /**
     * 绑定点击事件
     * @param listener 点击事件接口
     */
    fun setOnItemClickListener(listener: (view: View,position: Int) -> Unit) {
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
     * 是否过滤空数据
     */
    fun setNeedAutoFilterEmptyData(needAutoFilterEmptyData: Boolean) {
        this.needAutoFilterEmptyData = needAutoFilterEmptyData
    }

    /**
     * 设置点击事件
     * @param v 点击的视图
     */
    open fun clickEvent(v: View, position: Int) {
        if (!fastClick() && !click(v, position)) {
            if (needAutoSetClickIndex) {
                setClickIndex(position)
            }
            listener?.invoke(v, position)
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
     * 设置list
     */
    fun setDataList(data: List<T?>?) {
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

    /**
     * 列表中某个数据的位置
     */
    fun getIndex(data: T): Int = list.indexOf(data)

    /**
     * 刷新添加某个数据后的视图
     */
    open fun add(data: T) {
        addData(data)
        notifyItemInserted(itemCount)
    }

    /**
     * 刷新添加数据列表后的视图
     */
    open fun addAll(data: List<T?>) {
        addAllData(data)
        notifyItemRangeInserted(itemCount - data.size, data.size)
    }

    /**
     * 刷新移除某个位置的数据后的视图
     */
    open fun remove(position: Int) {
        removeData(position)
        notifyItemRemoved(position)
    }

    /**
     * 刷新全部数据
     */
    open fun refresh(data: List<T?>?) {
        setDataList(data)
        notifyDataSetChanged()
    }

    /**
     * 刷新某个数据
     */
    open fun replace(position: Int, data: T) {
        setData(position, data)
        notifyItemChanged(position)
    }

    /**
     * 适用于提前加载数据的情况
     */
    open fun getPreData(): MutableList<T?> {
        return ArrayList()
    }

    /**
     * 清空数据
     */
    open fun clear() {
        list.clear()
    }

    /**
     * 获取创建viewHolder时的view
     * 顺便取一下context
     */
    open fun getView(viewGroup: ViewGroup, layout: Int): View {
        if (context == null) {
            context = viewGroup.context
        }
        return LayoutInflater.from(viewGroup.context).inflate(layout, viewGroup, false)
    }

    /**
     * 根据viewType获取view
     */
    protected fun getViewByViewType(viewGroup: ViewGroup, viewType: Int): View {
        return getView(viewGroup, viewHolderLayoutIds[viewType])
    }

    /**
     * 获取一个可用的context
     */
    fun getContext(): Context = context?: getAppContext()

    /**
     * 设置item中控件的点击事件
     */
    protected fun setListener(view: View?, position: Int) {
        if (isItemIndex(position)) {
            view?.setOnClickListener {
                clickEvent(view, position)
            }
        }
    }

    /**
     * 设置item中控件的点击事件
     */
    protected fun setHolderListener(holder: BaseRecyclerViewHolder<*>) {
        val view = holder.itemView
        view.setOnClickListener {
            clickEvent(view, holder.layoutPosition)
        }
    }

    /**
     * 是否是数组下标
     */
    open fun isDataIndex(position: Int): Boolean = position in 0 until length

    /**
     * 是否是item下标
     */
    open fun isItemIndex(position: Int): Boolean = position in 0 until itemCount

    /**
     * item点击间隔
     */
    open fun getClickDoubleTime(): Int = 500

    /**
     * 拿一个全局context用来加载资源
     */
    private fun getAppContext(): Context = App.appContext

    /**
     * 处理内部点击事件
     * 可用于处理点击去重
     * @return 是否消费掉了此次点击事件
     */
    open fun click(v: View?, position: Int): Boolean = false

    interface OnItemClickListener {
        fun onClick(v: View?, position: Int)
    }

    /**
     * 根据资源id获取颜色
     */
    protected open fun getColor(resId: Int): Int = ContextCompat.getColor(getContext(), resId)

    /**
     * 根据资源id获取图片
     */
    protected open fun getDrawable(resId: Int): Drawable? = ContextCompat.getDrawable(getContext(), resId)

    /**
     * 根据资源id获取图片
     */
    protected open fun getDrawableWithBounds(resId: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(getContext(), resId)
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        return drawable
    }

    /**
     * 设置TextView文字旁边的图标
     * 0：文字左边图标
     * 1：文字上边图标
     * 2：文字右边图标
     * 3：文字下边图标
     */
    protected open fun setCompoundDrawable(
        view: TextView?,
        resId: Int,
        direction: Int
    ) {
        if (view != null) {
            when (direction) {
                0 -> view.setCompoundDrawables(getDrawableWithBounds(resId), null, null, null)
                1 -> view.setCompoundDrawables(null, getDrawableWithBounds(resId), null, null)
                2 -> view.setCompoundDrawables(null, null, getDrawableWithBounds(resId), null)
                3 -> view.setCompoundDrawables(null, null, null, getDrawableWithBounds(resId))
                else -> {
                }
            }
        }
    }

    /**
     * 设置控件显隐
     */
    fun View.setDisplay(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun View.setVisible(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * 某个view是否显示
     */
    fun View.isDisplay(view: View?): Boolean = view?.visibility == View.VISIBLE

    private var lastClickTime: Long = 0

    /**
     * 防止重复点击
     */
    private fun fastClick(): Boolean {
        val time = SystemClock.elapsedRealtime()
        return if (time - lastClickTime < getClickDoubleTime()){
            true
        }else{
            lastClickTime = time
            false
        }

    }

}