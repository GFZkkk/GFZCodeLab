package com.gfz.recyclerview.adapter

import com.gfz.common.utils.DateUtil
import com.gfz.common.utils.TopLog
import java.util.*
import kotlin.collections.ArrayList

/**
 * 日历适配器基类
 * 一般只需要实现生成bean的方法就可以用了。
 * 当加载的月份超过[partLimit]的两倍，将自动启动分步加载策略，也可以在后续手动设置[needLoadPartition]为false来关闭分步加载。
 * 分步加载将月的容器[monthList]为分两部分，每一部分大小为[partLimit]，当前在使用的为数据区，没被使用的为备用区。
 * 当月份变化触发[loadNextLimit]时，将会触发改变数据区的位置，更新备份区数据的方法。
 * created by gfz on 2020/4/6
 */
abstract class BaseCalendarAdapter<T>(
    sDate: String,
    eDate: String,
    nDate: String = DateUtil.getShortDateStr(),
    private val partLimit: Int = 10,
    private val loadNextLimit: Int = 3
) : BaseRecyclerViewAdapter<T>() {

    /**
     * 开始时间
     */
    private var startDate: IntArray = IntArray(3)

    /**
     * 结束时间
     */
    private var endDate: IntArray = IntArray(3)

    /**
     * 现在的时间
     */
    private var nowDate: IntArray = IntArray(3)

    /**
     * 显示的月的数量
     */
    private var monthNum = 0

    /**
     * 日历的数据
     */
    private var monthList: MutableList<List<T?>> = ArrayList(partLimit * 2)

    /**
     * 当前展示的月份是第几个月
     */
    private var curMonth = 0

    /**
     * 是否需要分步加载
     */
    private var needLoadPartition = false

    /**
     * 当前加载下标
     */
    private var curPartIndex = 0

    enum class MMDateEnum {
        BEFORE, AFTER, SAME
    }

    init {
        startDate = sDate.getDate()
        endDate = eDate.getDate()
        nowDate = when {
            compareTo(eDate, nDate) == MMDateEnum.AFTER -> eDate.getDate()
            compareTo(sDate, nDate) == MMDateEnum.BEFORE -> sDate.getDate()
            else -> nDate.getDate()
        }
        //是否需要分步加载
        if (monthNum > partLimit * 2) {
            needLoadPartition = true
        }
        //一共加载几个月的数据
        monthNum =
            (endDate.getYear() - startDate.getYear()) * 12 + endDate.getMonth() - startDate.getMonth() + 1
        //当前要显示哪个月
        curMonth =
            (nowDate.getYear() - startDate.getYear()) * 12 + nowDate.getMonth() - startDate.getMonth()
        //分步加载的月份下标
        if (needLoadPartition) {
            curPartIndex = curMonth % partLimit
            //最后一段数据和不是第一段数据但坐标处于前半部分的情况需要往后移
            if (curMonth / partLimit == monthNum / partLimit || curPartIndex < partLimit / 2 && curMonth / partLimit != 0) {
                curPartIndex += partLimit
            }
        }
        //支持不显示上个月数据的情况
        needAutoFilterEmptyData = false
    }

    /**
     * 生成自己的日历bean
     */
    abstract fun getCalendarBean(year: Int, month: Int, day: Int): T

    /**
     * 获取当前显示的年月
     */
    open fun getDateTime(): String {
        var year = startDate.getYear()
        var month = startDate.getMonth() + curMonth
        year += (month - 1) / 12
        month = (month - 1) % 12 + 1
        return "${year}年${month}月"
    }

    /**
     * 看下一个月
     */
    open fun laterMonth() {
        if (haveNext()) {
            if (needLoadPartition) {
                if (curPartIndex < partLimit * 2 - 1) {
                    curPartIndex++
                    curMonth++
                    show()
                } else {
                    TopLog.i("数据加载中")
                }
            } else {
                curMonth++
                show()
            }
        }
    }

    /**
     * 看上一个月
     */
    open fun preMonth() {
        if (havePre()) {
            if (needLoadPartition) {
                if (curPartIndex > 0) {
                    curPartIndex--
                    curMonth--
                    show()
                } else {
                    TopLog.i("数据加载中")
                }
            } else {
                curMonth--
                show()
            }
        }
    }

    /**
     * 展示日历
     */
    open fun show() {
        checkMonthList()
        if (needLoadPartition) {
            refresh(monthList[curPartIndex])
        } else {
            refresh(monthList[curMonth])
        }
    }

    /**
     * 是否还有前一个月的数据
     */
    open fun havePre() = curMonth > 0

    /**
     * 是否还有下一个月的数据
     */
    open fun haveNext() = curMonth < monthNum - 1

    /**
     * 检查每个月的数据
     */
    private fun checkMonthList() {
        if (needLoadPartition) {
            val start = curMonth - curPartIndex % partLimit
            if (monthList.isNotEmpty()) {
                if (curPartIndex <= loadNextLimit && curMonth / partLimit != 0) {
                    TopLog.e("数据区后移")
                    monthList.move(true)
                    loadDataList(start, 0, partLimit)
                    curPartIndex += partLimit
                } else if (curPartIndex >= partLimit * 2 - loadNextLimit && curMonth / partLimit != monthNum / partLimit) {
                    TopLog.e("数据区前移")
                    monthList.move(false)
                    loadDataList(start, partLimit, partLimit * 2)
                    curPartIndex -= partLimit
                }
            } else {
                //如果下标靠前且不是第一序列则数据区加载到后半部分
                if (curMonth > partLimit && curPartIndex % partLimit < partLimit / 2) {
                    loadDataList(start - partLimit, 0, partLimit * 2)
                } else {
                    loadDataList(start, 0, partLimit * 2)
                }

            }
        } else {
            if (monthList.isEmpty()) {
                loadDataList()
            }

        }
    }

    /**
     * 加载持有的月份数据
     * @param start 开始月份下标
     * @param startIndex 填充的起始下标
     * @param endIndex 填充的结束下表
     */
    private fun loadDataList(start: Int = 0, startIndex: Int = 0, endIndex: Int = monthNum) {
        val year = startDate.getYear()
        val startMonth = startDate.getMonth()
        for (i in startIndex until endIndex) {
            val month = startMonth + start + i
            if (month >= monthNum + startMonth) {
                break
            }
            monthList.add(i, getDayList(year + (month - 1) / 12, (month - 1) % 12 + 1))
        }
    }

    /**
     * 得到每个月的天数的列表
     */
    private fun getDayList(year: Int, month: Int): List<T?> {
        val dayList: MutableList<T?> = ArrayList()
        val dateTime: Calendar = Calendar.getInstance()
        dateTime.set(year, month - 1, 1)
        //补足空白天数
        for (i in 1 until dateTime.get(Calendar.DAY_OF_WEEK)) {
            dayList.add(null)
        }
        //加上有效天数
        for (i in 0 until DateUtil.getMonthDay(year, month)) {
            dayList.add(getCalendarBean(year, month, i + 1))
        }
        //固定42天
        for (i in 0 until 42 - dayList.size) {
            dayList.add(null)
        }
        return dayList
    }

    /**
     * 判断 2019-08-04 与 2020-07-06 的先后关系
     * return 2020-07-06 是否在 2019-08-04 之后
     */
    fun compareTo(date1: String, date2: String): MMDateEnum {
        val dateTime1 = date1.getDate()
        val dateTime2 = date2.getDate()
        return compareTo(dateTime1, dateTime2)
    }

    /**
     * 判断 2019-08-04 与 2020-07-06 的先后关系
     * return 2020-07-06 是否在 2019-08-04 之后
     */
    fun compareTo(dateTime1: IntArray, dateTime2: IntArray): MMDateEnum {
        for (i in dateTime1.indices) {
            val time1 = dateTime1[i]
            val time2 = dateTime2[i]
            if (time1 < time2) {
                return MMDateEnum.AFTER
            } else if (time1 > time2) {
                return MMDateEnum.BEFORE
            }
        }
        return MMDateEnum.SAME
    }

    /**
     * 获取展示的月的数量
     */
    open fun getMonthNum(): Int {
        return monthNum
    }

    /**
     * 获取日历开始日期
     */
    open fun getStartDate(): IntArray {
        return startDate
    }

    /**
     * 获取日历结束日期
     */
    open fun getEndDate(): IntArray {
        return endDate
    }

    /**
     * 获取现在的日期
     */
    open fun getNowDate(): IntArray {
        return nowDate
    }

    fun IntArray.getYear(): Int {
        require(this.size == 3)
        return this[0]
    }

    fun IntArray.getMonth(): Int {
        require(this.size == 3)
        return this[1]
    }

    fun IntArray.getDay(): Int {
        require(this.size == 3)
        return this[2]
    }

    fun IntArray.isToday(): MMDateEnum {
        return compareTo(nowDate, this)
    }

    fun String.getDate(delimiters: String = "-"): IntArray {
        val s: List<String> = this.split(delimiters)
        require(s.size == 3)
        return s.map { it.toInt() }.toIntArray()
    }

    private fun MutableList<List<T?>>.move(isNext: Boolean) {
        require(this.count() > 1)
        val num: Int = this.count() / 2
        val partNum = if (isNext) this.count() - num else 0
        this.filterIndexed { index, _ ->
            !(isNext xor (index < num))
        }.forEachIndexed { index, v ->
            this[index + partNum] = v
        }
    }
}