package com.gfz.lab.ui.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.gfz.lab.utils.TimeCell

/**
 * created by gaofengze on 2020/4/30
 */

abstract class BaseFragment: Fragment(), BasePageTools {

    val handler by lazy{
        Handler(Looper.getMainLooper())
    }

    private val timeCell: TimeCell by lazy {
        TimeCell()
    }

    var mActivity: BaseActivity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    protected abstract fun initView()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseActivity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun start(activity: Class<out BaseActivity>, bundle: Bundle?) {
        mActivity?.start(activity, bundle)
    }

    override fun start(action: Int, bundle: Bundle?) {
        mActivity?.start(action, bundle)
    }

    override fun showToast(text: String) {
        mActivity?.showToast(text)
    }

    override fun showToast(textRes: Int) {
        mActivity?.showToast(textRes)
    }

    override fun fastClick(tag: Int, dur: Int) = timeCell.fastClick(tag, dur)
}