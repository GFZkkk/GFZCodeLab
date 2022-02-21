package com.gfz.lab.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gfz.common.ext.getCompatColor
import com.gfz.common.ext.setVisible
import com.gfz.common.utils.TimeCell
import com.gfz.common.utils.ToastUtil
import com.gfz.lab.R


/**
 * created by gaofengze on 2020-01-19
 */

abstract class BaseActivity : AppCompatActivity(), BasePageTools {

    lateinit var nav: NavController

    var handler: Handler? = null

    val taskList: HashSet<Runnable> = HashSet()

    private val timeCell: TimeCell by lazy {
        TimeCell()
    }

    abstract fun loadView()

    open fun initView() {}

    abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowStatus()
        loadView()
        handler = Handler(mainLooper)
        getNavId()?.apply {
            nav = getNavControllerById(this)
        }

        initView()
        initData()
    }

    override fun onDestroy() {
        handler?.removeMessages(0)
        handler = null
        super.onDestroy()
    }

    open fun setWindowStatus() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = getCompatColor(R.color.col_b07529)
        }
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
    override fun start(activity: Class<out BaseActivity>, bundle: Bundle?) {
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

    fun postDelayed(runnable: Runnable, delayed: Long) {
        handler?.postDelayed(runnable, delayed)
    }

    /**
     * 显示吐司
     */
    override fun showToast(text: String) {
        ToastUtil.showToast(text)
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
        findViewById<View>(R.id.loading).setVisible(show)
    }

    // endregion
}