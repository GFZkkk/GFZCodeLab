package com.gfz.lab.ui.fragment.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.common.utils.RandomUtil
import com.gfz.common.utils.TopLog
import com.gfz.lab.adapter.TestExtAdapter
import com.gfz.lab.databinding.FragmentExtlayoutBinding
import com.gfz.lab.databinding.LayoutHeaderTestBinding
import com.gfz.lab.base.BaseVBFragment
import com.gfz.common.utils.viewBind
import com.gfz.lab.R
import com.gfz.lab.databinding.LayoutEmptyTestBinding
import com.gfz.lab.databinding.LayoutFooterTestBinding

/**
 *
 * created by gaofengze on 2021/2/22
 */
class TestExtAdapterFragment : BaseVBFragment<FragmentExtlayoutBinding>() {

    val adapter by lazy {
        TestExtAdapter().apply {
            setOnItemClickDataListener { view, i, number ->
                when(view.id){
                    R.id.tv_add -> {
                        add(number){
                            TopLog.e("新增数据")
                            toLogAdapter {
                                haveHeader = RandomUtil.getRandomBoolean()
                                haveFooter = RandomUtil.getRandomBoolean()
                            }
                        }
                    }
                    R.id.tv_delete -> {

                        remove(i){
                            TopLog.e("删除数据：$number")
                            toLogAdapter {
                                haveHeader = RandomUtil.getRandomBoolean()
                                haveFooter = RandomUtil.getRandomBoolean()
                            }
                        }
                    }
                    R.id.tv_update -> {
                        replace(i, RandomUtil.getRandomIndex(1000))
                    }
                    R.id.tv_insert -> {
                        add(number, i){
                            TopLog.e("新增数据")
                            toLogAdapter {
                                haveHeader = RandomUtil.getRandomBoolean()
                                haveFooter = RandomUtil.getRandomBoolean()
                            }
                        }
                    }
                }
            }
        }
    }
    override fun initView() {

        with(binding) {
            rvList.adapter = adapter
            rvList.layoutManager = LinearLayoutManager(requireContext())
            adapter.refresh(listOf(1, 2, 3, 4))
            val emptyBinding: LayoutEmptyTestBinding = viewBind(rvList)
            adapter.emptyViewBinding = emptyBinding

            val headerBinding: LayoutHeaderTestBinding = viewBind(rvList)
            adapter.headerViewBinding = headerBinding

            val footerTestBinding: LayoutFooterTestBinding = viewBind(rvList)
            adapter.footerViewBinding = footerTestBinding

            headerBinding.tvHeader.text = "测试ccc"
            footerTestBinding.tvFooter.text = "货真价实足布局"
            emptyBinding.tvEmpty.text = "可怕的空白"

            emptyBinding.tvEmpty.setOnClickListener {

                adapter.add(RandomUtil.getRandomIndex(100)){
                    TopLog.e("新增数据")
                    toLogAdapter {
                        adapter.haveHeader = RandomUtil.getRandomBoolean()
                        adapter.haveFooter = RandomUtil.getRandomBoolean()
                    }
                }
            }
        }
    }

    private fun toLogAdapter(block: () -> Unit){
        val oh = adapter.haveHeader
        val of = adapter.haveFooter
        block()
        val nh = adapter.haveHeader
        val nf = adapter.haveFooter
        if (oh == nh){
            TopLog.e("头布局无变化")
        } else {
            if (nh){
                TopLog.e("新增头布局")
            } else {
                TopLog.e("删除头布局")
            }
        }

        if (of == nf){
            TopLog.e("足布局无变化")
        } else {
            if (nf) {
                TopLog.e("新增足布局")
            } else {
                TopLog.e("删除足布局")
            }
        }
    }

    override fun getTitleText(): String {
        return "扩展布局实验区"
    }
}