package com.gfz.lab.ui.fragment.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.gfz.common.utils.RandomUtil
import com.gfz.ui.base.util.viewBind
import com.gfz.lab.adapter.TestExtAdapter
import com.gfz.lab.databinding.FragmentExtlayoutBinding
import com.gfz.lab.databinding.LayoutHeaderTestBinding
import com.gfz.lab.R
import com.gfz.lab.databinding.LayoutEmptyTestBinding
import com.gfz.lab.databinding.LayoutFooterTestBinding
import com.gfz.ui.base.page.BaseVBFragment

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
                        add(number)
                    }
                    R.id.tv_delete -> {

                        remove(i)
                    }
                    R.id.tv_update -> {
                        replace(i, RandomUtil.getRandomIndex(1000))
                    }
                    R.id.tv_insert -> {
                        add(number, i + 1)
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

                adapter.add(RandomUtil.getRandomIndex(100))
            }

            footerTestBinding.tvFooter.setOnClickListener {
                adapter.refresh(ArrayList<Number?>().apply {
                    repeat(RandomUtil.getRandomIndex(10)){
                        add(RandomUtil.getRandomIndex(100))
                    }
                })
            }
        }
    }

    override fun getTitleText(): String {
        return "扩展布局实验区"
    }
}