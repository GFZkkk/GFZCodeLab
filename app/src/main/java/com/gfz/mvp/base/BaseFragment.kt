package com.gfz.mvp.base

import android.content.Context
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gfz.mvp.data.App

/**
 * created by gaofengze on 2020/4/30
 */

abstract class BaseFragment : Fragment(){

    private val handler = Handler()

    var mActivity: BaseActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseActivity
    }

    protected abstract fun initView()

    protected abstract fun loadData()



    protected fun showToast(text: String){
        Toast.makeText(App.appContext, text, if(text.length > 10){ Toast.LENGTH_LONG }else{ Toast.LENGTH_SHORT }).show()
    }

    protected fun showToast(textRes: Int){
        showToast(App.appContext.getString(textRes))
    }

}