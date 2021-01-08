package com.gfz.mvp.ui.activity

import android.text.Editable
import android.text.TextWatcher
import com.gfz.mvp.R
import com.gfz.mvp.base.BaseActivity
import com.gfz.mvp.utils.Content
import com.gfz.mvp.utils.TopLog
import kotlinx.android.synthetic.main.activity_record.*
import androidx.core.widget.addTextChangedListener as addTextChangedListener

/**
 * Created by gaofengze on 2020/9/15.
 */
class TestAnimationActivity : BaseActivity()  {

    override fun getLayoutId(): Int {
        return R.layout.activity_record
    }

    override fun initView() {

    }

    override fun initData() {
        fw.start()
        fw.setOnClickListener {
            fw.start()
        }
    }
}