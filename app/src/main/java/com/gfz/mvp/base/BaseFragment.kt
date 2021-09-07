package com.gfz.mvp.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import com.gfz.mvp.utils.TimeCell
import com.gfz.mvp.utils.toLog
import java.lang.Exception

/**
 * created by gaofengze on 2020/4/30
 */

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId), BasePageTools{

    val handler by lazy{
        Handler(Looper.getMainLooper())
    }

    private val timeCell: TimeCell by lazy {
        TimeCell()
    }

    var mActivity: BaseActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    protected abstract fun initView()

    override fun start(baseActivity: BaseActivity, bundle: Bundle?) {

    }

    override fun start(baseFragment: BaseFragment, bundle: Bundle?) {

    }

    override fun startForResult(baseActivity: BaseActivity, bundle: Bundle?, request: Int) {

    }

    override fun startForResult(baseFragment: BaseFragment, bundle: Bundle?, request: Int) {

    }

    fun handlerFragment(operation: (transition: FragmentTransaction) -> Unit){
        try{
            mActivity?.supportFragmentManager?.beginTransaction()?.apply {
                operation(this)
                this.commitAllowingStateLoss()
            }
        }catch (e: Exception){
            e.message?.toLog()
        }

    }

    override fun showToast(text: String) {
        mActivity?.showToast(text)
    }

    override fun showToast(textRes: Int) {
        mActivity?.showToast(textRes)
    }

    override fun fastClick(tag: Int, dur: Int) = timeCell.fastClick(tag, dur)
}