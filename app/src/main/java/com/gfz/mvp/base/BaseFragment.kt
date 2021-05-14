package com.gfz.mvp.base

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gfz.mvp.data.KTApp
import com.gfz.mvp.utils.TimeCell

/**
 * created by gaofengze on 2020/4/30
 */

abstract class BaseFragment : Fragment(), BasePageTools{

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

    protected abstract fun initView()

    protected abstract fun loadData()

    override fun showToast(text: String) {
        mActivity?.showToast(text)
    }

    override fun showToast(textRes: Int) {
        mActivity?.showToast(textRes)
    }

    override fun fastClick(tag: Int, dur: Int) = timeCell.fastClick(tag, dur)
}