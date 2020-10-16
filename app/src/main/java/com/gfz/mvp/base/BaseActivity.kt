package com.gfz.mvp.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gfz.mvp.data.KTApp
import com.gfz.mvp.utils.ToastUtil

/**
 * created by gaofengze on 2020-01-19
 */

abstract class BaseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initData()
    }

    protected abstract fun getLayoutId() : Int

    protected abstract fun initView()

    protected abstract fun initData()

    protected fun showToast(text: String){
        ToastUtil.showToast(text)
    }

    protected fun showToast(textRes: Int){
        showToast(getString(textRes))
    }

}