package com.gfz.lab.ui.fragment.home

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gfz.common.ext.*
import com.gfz.lab.adapter.TestClockAdapter
import com.gfz.recyclerview.adapter.BaseCenterAdapter
import com.gfz.common.utils.TopLog
import com.gfz.lab.R
import com.gfz.lab.databinding.FragmentCustomBinding
import com.gfz.lab.utils.MessageCallback
import com.gfz.lab.utils.WebSocketUtil
import com.gfz.recyclerview.decoration.NormalDecoration
import com.gfz.ui.base.page.BaseVBFragment
import kotlin.math.abs

/**
 * Created by gaofengze on 2020/7/2.
 */
class TestCustomFragment : BaseVBFragment<FragmentCustomBinding>(), MessageCallback{

    val adapter by lazy {
        TestClockAdapter(requireContext())
    }
    private var curIndex = 0
    private var chooseTime: Int? = 5

    override fun getTitleText(): String {
        return "时钟实验区"
    }

    override fun initView() {
        lifecycleScope.launchSafe {
            val socket = WebSocketUtil.getWebSocketByUrl(WebSocketUtil.TEST_URL)
            socket.addListener(this@TestCustomFragment)
            socket.connect()
            socket.sendMessage("hello")

        }
        // 截图
        /*handler.postDelayed({
            lifecycleScope.launchSafe {
                val bitmap = BitmapUtil.convertViewToBitmap(
                    requireView(),
                    300.toPX(),
                    300.toPX(),
                    backgroundColor = getColor(R.color.transparent),
                    config = Bitmap.Config.ARGB_8888
                )
                binding.ivTest.setImageBitmap(bitmap)
                binding.ivTest.setVisible(true)
            }
        }, 2000)*/
        with(binding) {

            tvMarquee.isSelected = true

            tvMarquee.text = getTextContent()
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

    private fun getTextContent(): CharSequence {
        val list = listOf(
            "第一条第一条第一条第一条",
            "第二条第二条",
            "第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条",
            "第四条",
        )
        val text: StringBuilder = StringBuilder()
        list.forEach {
            text.append(it)
            text.append("                  ")
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

    override fun onMessage(text: String) {
        showToast(text)
    }
}