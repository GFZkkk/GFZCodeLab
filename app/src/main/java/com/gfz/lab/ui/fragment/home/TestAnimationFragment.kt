package com.gfz.lab.ui.fragment.home

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import com.gfz.common.utils.TopLog
import com.gfz.lab.R
import com.gfz.lab.databinding.FragmentAnimationBinding
import com.gfz.ui.base.page.BaseVBFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by gaofengze on 2020/9/15.
 */
class TestAnimationFragment : BaseVBFragment<FragmentAnimationBinding>() {

    override fun initView() {
        with(binding) {
//            fw.start()
            fw.setOnClickListener {
                fw.start()
            }
            interpolatorView.start()
        }
//        Handler(Looper.getMainLooper()).postDelayed({
//            lifecycleScope.launch {
//                TopLog.e("测试开始:${Thread.currentThread().name}")
//                withContext(Dispatchers.Main){
//                    TopLog.e("测试:${Thread.currentThread().name}")
//                    Thread.sleep(50000)
//                }
//                TopLog.e("测试结束:${Thread.currentThread().name}")
//            }
//        }, 100)

    }

    override fun getTitleText(): String {
        return "自定义动画实验区"
    }
}