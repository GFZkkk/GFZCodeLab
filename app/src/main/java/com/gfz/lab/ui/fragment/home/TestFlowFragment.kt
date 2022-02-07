package com.gfz.lab.ui.fragment.home

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.gfz.common.ext.setDisplay
import com.gfz.common.ext.setVisible
import com.gfz.common.ext.toDP
import com.gfz.common.ext.toPX
import com.gfz.common.utils.TopLog
import com.gfz.lab.databinding.FragmentFlowBinding
import com.gfz.lab.base.BaseVMFragment
import com.gfz.lab.viewmodel.FlowViewModel

/**
 * Created by xueya on 2022/1/11
 */
class TestFlowFragment() : BaseVMFragment<FragmentFlowBinding, FlowViewModel>() {
    override val viewModel: FlowViewModel by viewModels()

    override fun initView() {
        with(binding){
            btnStart.setOnClickListener {
                viewModel.start()
            }
            btnStartSingle.setOnClickListener {
                viewModel.startSingle()
            }
            btnReStart.setOnClickListener {
                viewModel.reStart()
            }
            btnStop.setOnClickListener {
                viewModel.stop()
            }
        }
    }

    override fun initData() {
        viewModel.getData()
    }

    override fun initObserver() {
        viewModel.dataList.observe(this){
            it.forEachIndexed{ index, data ->
                addView(index, data)
            }
            hideLeftView(it.size)
            binding.flContent.requestLayout()
        }
    }

    private fun addView(index: Int, content: String){
        with(binding){
            if (flContent.childCount <= index){
                binding.flContent.addView(TextView(context).apply {
                    text = content
                    layoutParams = ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        leftMargin = 5.toPX()
                        rightMargin = 5.toPX()
                        topMargin = 3.toPX()
                        bottomMargin = 3.toPX()

                    }
                })
            } else {
                (flContent.getChildAt(index) as? TextView)?.apply {
                    text = content
                    setVisible(true)
                }
            }
        }
    }

    private fun hideLeftView(size: Int){
        with(binding){
            TopLog.e("$size ${flContent.childCount}")
            if (size >= flContent.childCount){
                return
            }
            (size .. flContent.childCount).forEach {
                flContent.getChildAt(it).setDisplay(false)
            }
        }

    }
}