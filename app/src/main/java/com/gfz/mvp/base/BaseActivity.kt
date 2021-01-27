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

    private val FAST_TAG_DEFAULT = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    protected open fun initView(){}

    protected abstract fun initData()

    protected fun getContent() = this
    /**
     * 显示吐司
     */
    protected fun showToast(text: String){
        ToastUtil.showToast(text)
    }

    protected fun showToast(textRes: Int){
        showToast(getString(textRes))
    }

    /**
     * 防重复点击，或者防重复调用
     * 也可以判断是否是连续调用
     * @param dur 调用间隔
     * @return 是否连续调用
     */
    protected fun fastClick(dur: Int) = timeCell.fastClick(FAST_TAG_DEFAULT, dur)

    /**
     * @param tag 调用群组标记
     */
    protected fun fastClick(tag: Int, dur: Int) = timeCell.fastClick(tag, dur)



}