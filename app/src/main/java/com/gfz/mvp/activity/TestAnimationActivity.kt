package com.gfz.mvp.activity

import com.gfz.mvp.base.BaseActivity
import com.gfz.mvp.databinding.ActivityAnimationBinding
import com.gfz.mvp.utils.viewBind

/**
 * Created by gaofengze on 2020/9/15.
 */
class TestAnimationActivity : BaseActivity()  {

    private val binding : ActivityAnimationBinding by viewBind()

    override fun initData() {
        with(binding){
            fw.start()
            fw.setOnClickListener {
                fw.start()
            }
        }
    }


}