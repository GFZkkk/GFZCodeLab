package com.gfz.ui.base.page

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gfz.common.utils.TimeCell
import com.gfz.common.utils.ToastUtil
import com.gfz.ui.base.dialog.LoadingDialog
import com.gfz.ui.base.interfaces.BasePageTools


/**
 * created by gaofengze on 2020-01-19
 */

abstract class BaseActivity : AppCompatActivity(), BasePageTools {

    lateinit var nav: NavController

    private val loadingDialog by lazy {
        LoadingDialog()
    }

    val handler: Handler by lazy {
        Handler(mainLooper)
    }

    val timeCell: TimeCell by lazy {
        TimeCell()
    }

    abstract fun loadView()

    open fun initView() {}

    abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowStatus()
        loadView()
        getNavId()?.apply {
            nav = getNavControllerById(this)
        }
        initView()
        initData()
    }

    override fun onDestroy() {
        handler.removeMessages(0)
        super.onDestroy()
    }

    protected open fun setWindowStatus() {

    }

    @IdRes
    open fun getNavId(): Int? {
        return null
    }

    private fun getNavControllerById(id: Int): NavController {
        val navHostFragment = supportFragmentManager.findFragmentById(id) as NavHostFragment
        return navHostFragment.navController
    }

    fun getContext() = this

    // region 工具方法
    override fun start(activity: Class<out Activity>, bundle: Bundle?) {
        val intent = Intent(this, activity)
        bundle?.apply {
            intent.putExtras(this)
        }
        startActivity(intent)
    }

    override fun start(@IdRes action: Int, bundle: Bundle?) {
        nav.navigate(action, bundle)
    }

    override fun pop() {
        nav.popBackStack()
    }

    override fun popTo(action: Int, inclusive: Boolean) {
        nav.popBackStack(action, inclusive)
    }

    /**
     * 显示吐司
     */
    override fun showToast(text: String) {
        runOnUiThread {
            ToastUtil.showToast(text)
        }
    }

    override fun showToast(textRes: Int) {
        showToast(getString(textRes))
    }

    /**
     * 防重复点击，或者防重复调用
     * 也可以判断是否是连续调用
     * @param dur 调用间隔
     * @return 是否连续调用
     */
    override fun fastClick(tag: Int, dur: Int) = timeCell.fastClick(tag, dur)

    override fun addIdleTask(keep: Boolean, block: () -> Unit) {
        Looper.myQueue().addIdleHandler {
            block()
            keep
        }
    }

    override fun showLoading(show: Boolean) {
        if (loadingDialog.isResumed == show) return
        if (show){
            loadingDialog.showNow(supportFragmentManager, "loading")
        } else {
            loadingDialog.dismiss()
        }
    }

    // endregion
}