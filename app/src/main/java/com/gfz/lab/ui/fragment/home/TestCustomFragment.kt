package com.gfz.lab.ui.fragment.home

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gfz.lab.adapter.TestClockAdapter
import com.gfz.recyclerview.adapter.BaseCenterAdapter
import com.gfz.lab.base.BaseVBFragment
import com.gfz.common.utils.TopLog
import com.gfz.common.ext.toPX
import com.gfz.common.utils.ScreenUtil
import com.gfz.lab.databinding.FragmentCustomBinding
import com.gfz.recyclerview.decoration.NormalDecoration
import java.lang.ref.WeakReference
import kotlin.math.abs

/**
 * Created by gaofengze on 2020/7/2.
 */
class TestCustomFragment : BaseVBFragment<FragmentCustomBinding>() {

    val adapter by lazy {
        TestClockAdapter(requireContext())
    }
    private var curIndex = 0
    private var chooseTime: Int? = 5

    override fun getTitleText(): String {
        return "时钟实验区"
    }

    override fun initView() {
        with(binding){

            tvMarquee.isSelected = true

            val limit = (ScreenUtil.getScreenWidth(tvMarquee.context) / tvMarquee.textSize).toInt()
            tvMarquee.text = getTextContent(limit)
            tvMarquee.postTask {
                TopLog.e(width)
            }
            v1.setOnClickListener {
                v2.bringToFront()
            }

            v2.setOnClickListener {
                v1.bringToFront()
            }

            rvList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rvList.addItemDecoration(NormalDecoration(0, 90.toPX()))
            rvList.adapter = adapter
            val timeItems: MutableList<Int?> = ArrayList()
            for (i in 1..12) {
                timeItems.add(i * 5)
            }
            adapter.refresh(timeItems)

            adapter.setOnItemChangeListener(object : BaseCenterAdapter.OnItemChangeListener {
                override fun onItemChange(position: Int) {
                    tvClock.text = String.format("%s:00", adapter.getData(position))
                    chooseTime = adapter.getData(position)
                }
            })

            adapter.setOnItemScrollListener(object : BaseCenterAdapter.OnItemScrollListener {
                override fun onItemScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    val index: Int = getIndexByOffset(position, positionOffset)
                    if (index != -1 && index != curIndex) {
                        curIndex = index
//                    play(curIndex, context, R.raw.keyboard, null)
                    }
                }
            })

            val position = chooseTime!! / 5 - 1
            adapter.scrollToPosition(position)
            tvClock.text = String.format("%s:00", adapter.getData(position))
        }
    }

    fun getTextContent(limit: Int): CharSequence{
        val list = listOf(
            "第一条第一条第一条第一条",
            "第二条第二条",
            "第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条",
            "第四条",
        )
        val text: StringBuilder = StringBuilder()

        repeat(limit){
            text.append("   ")
        }
        TopLog.e(text.length)
        list.forEach {
            text.append(it)
            text.append("                              ")
        }
        return text
    }

    private fun getIndexByOffset(position: Int, positionOffset: Float): Int {
        val offset = abs((positionOffset * 100).toInt())
        val x = 5
        val a = intArrayOf(0, 20, 40, 60, 80)
        val range = 100
        var r = offset + x
        var r1 = offset - x
        if (r >= range) {
            r = 0
        }
        if (r1 < 0) {
            r1 = 0
        }
        for (j in a.indices) {
            if (a[j] in r1..r) {
                return j
            }
        }
        if (curIndex != -1 && curIndex < a.size - 1) {
            if (r >= a[curIndex + 1]) {
                TopLog.e(String.format("拯救：%s", curIndex + 1))
                return curIndex + 1
            }
        }
        return -1
    }

    fun View.postTask(delay: Long = 0, task: View.() -> Unit){
        postDelayed(object : ViewRunnable<View>(WeakReference(this)){
            override fun run(view: View) {
                task(view)
            }
        }, delay)
    }

    /*fun TextView.postTask(delay: Long = 0, task: TextView.() -> Unit){
        postDelayed(object : ViewRunnable<TextView>(WeakReference(this)){
            override fun run(view: TextView) {
                task(view)
            }
        }, delay)
    }*/

    abstract class ViewRunnable<T: View>(private val view: WeakReference<T>) : Runnable{
        override fun run() {
            view.get()?.let {
                run(it)
            }
        }

        abstract fun run(view: T)
    }
}