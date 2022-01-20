package com.gfz.lab.ui.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gfz.lab.R

import com.gfz.common.utils.TimeCell

/**
 * created by gaofengze on 2020/4/30
 */

abstract class BaseFragment : Fragment(), BasePageTools {
    lateinit var nav: NavController

    val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val timeCell: TimeCell by lazy {
        TimeCell()
    }

    var mActivity: BaseActivity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nav = findNavController()
        setTitleView(view)
        initView()
    }

    protected abstract fun initView()

    protected open fun getBackViewId() = R.id.tv_back

    protected open fun getTitleText(): String? = null

    open fun setTitleView(view: View) {

        view.findViewById<TextView>(getBackViewId())?.setOnClickListener {
            if (fastClick()) {
                return@setOnClickListener
            }
            pop()
        }

        getTitleText()?.also {
            view.findViewById<TextView>(R.id.tv_header_title)?.text = it
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as? BaseActivity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun start(activity: Class<out BaseActivity>, bundle: Bundle?) {
        mActivity?.start(activity, bundle)
    }

    override fun start(action: Int, bundle: Bundle?) {
        nav.navigate(action, bundle)
    }

    override fun pop() {
        nav.navigateUp()
    }

    override fun popTo(action: Int, inclusive: Boolean) {
        nav.popBackStack(action, inclusive)
    }

    override fun showToast(text: String) {
        mActivity?.showToast(text)
    }

    override fun showToast(textRes: Int) {
        mActivity?.showToast(textRes)
    }

    override fun fastClick(tag: Int, dur: Int) = timeCell.fastClick(tag, dur)
}