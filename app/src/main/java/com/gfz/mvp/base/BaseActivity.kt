package com.gfz.mvp.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.gfz.mvp.R
import com.gfz.mvp.utils.TimeCell
import com.gfz.mvp.utils.ToastUtil
import com.gfz.mvp.utils.getCompatColor


/**
 * created by gaofengze on 2020-01-19
 */

abstract class BaseActivity : AppCompatActivity(), BasePageTools{

    val handler by lazy{
        Handler(Looper.getMainLooper())
    }

    private val timeCell: TimeCell by lazy {
        TimeCell()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowStatus()
        initView()
        initData()
    }

    private fun setWindowStatus(){
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getCompatColor(R.color.white)
    }

    open fun initView(){}

    abstract fun initData()

    // region 工具方法
    fun getContext() = this
    /**
     * 显示吐司
     */
    override fun showToast(text: String){
        ToastUtil.showToast(text)
    }

    override fun showToast(textRes: Int){
        showToast(getString(textRes))
    }

    /**
     * 防重复点击，或者防重复调用
     * 也可以判断是否是连续调用
     * @param dur 调用间隔
     * @return 是否连续调用
     */
    override fun fastClick(tag: Int, dur: Int) = timeCell.fastClick(tag, dur)
    // endregion
}