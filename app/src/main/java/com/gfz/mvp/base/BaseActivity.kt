package com.gfz.mvp.base

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.gfz.mvp.utils.TimeCell
import com.gfz.mvp.utils.ToastUtil

/**
 * created by gaofengze on 2020-01-19
 */

abstract class BaseActivity : AppCompatActivity(){

    val handler by lazy{
        Handler()
    }

    private val timeCell: TimeCell by lazy {
        TimeCell()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    open fun initView(){}

    abstract fun initData()

    fun getContext() = this
    /**
     * 显示吐司
     */
    fun showToast(text: String){
        ToastUtil.showToast(text)
    }

    fun showToast(textRes: Int){
        showToast(getString(textRes))
    }

    /**
     * 防重复点击，或者防重复调用
     * 也可以判断是否是连续调用
     * @param dur 调用间隔
     * @return 是否连续调用
     */
    fun fastClick(tag: Int = 0, dur: Int = 500) = timeCell.fastClick(tag, dur)
}