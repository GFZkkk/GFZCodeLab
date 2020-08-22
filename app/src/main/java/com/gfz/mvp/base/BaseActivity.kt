package com.gfz.mvp.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gfz.mvp.data.App

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
        Toast.makeText(App.appContext, text, if(text.length > 10){ Toast.LENGTH_LONG }else{ Toast.LENGTH_SHORT }).show()
    }

    protected fun showToast(textRes: Int){
        showToast(App.appContext.getString(textRes))
    }

}