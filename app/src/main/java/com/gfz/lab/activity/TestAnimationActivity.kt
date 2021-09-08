package com.gfz.lab.activity

import com.gfz.lab.base.BaseActivity
import com.gfz.lab.databinding.ActivityAnimationBinding
import com.gfz.lab.utils.viewBind

/**
 * Created by gaofengze on 2020/9/15.
 */
class TestAnimationActivity : BaseActivity()  {

    private val binding : ActivityAnimationBinding by viewBind()

    override fun initData() {
        with(binding){
//            fw.start()
            fw.setOnClickListener {
                fw.start()
            }
            interpolatorView.start()
        }

    }


}