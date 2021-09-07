package com.gfz.mvp.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.gfz.mvp.R
import com.gfz.mvp.utils.*
import java.lang.Exception


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

    override fun start(baseActivity: BaseActivity, bundle: Bundle?) {
        val intent = Intent(this, baseActivity.javaClass)
        bundle?.apply {
            intent.putExtras(this)
        }
        startActivity(intent)
    }

    override fun start(baseFragment: BaseFragment, bundle: Bundle?) {
        handlerFragment {
            it.add(R.id.fl_body, baseFragment)
        }
    }

    override fun startForResult(baseActivity: BaseActivity, bundle: Bundle?, request: Int) {
        val intent = Intent(this, baseActivity.javaClass)
        bundle?.apply {
            intent.putExtras(this)
        }
        startActivityForResult(intent, request)
    }

    override fun startForResult(baseFragment: BaseFragment, bundle: Bundle?, request: Int) {

    }

    fun handlerFragment(operation: (transition: FragmentTransaction) -> Unit){
        try{
            val transition = supportFragmentManager.beginTransaction()
            operation(transition)
            transition.commitAllowingStateLoss()
        }catch (e: Exception){
            e.message?.toLog()
        }

    }

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