package com.gfz.mvp.module

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.gfz.mvp.data.KTApp

/**
 * 管理小窗口布局
 * created by gaofengze on 2021/2/22
 */
object OverlayManager {
    //要引用的布局文件.
    private var rootView: View? = null

    //布局参数.
    private val params: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams()
    }

    //实例化的WindowManager.
    private val windowManager: WindowManager by lazy {
        KTApp.appContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    init {
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }else{
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        //设置窗口初始停靠位置.
        params.gravity = Gravity.RIGHT or Gravity.TOP
    }

    fun showWindow(view: View){
        rootView = view
        //主动计算出当前View的宽高信息.
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.post {
            params.width = view.measuredWidth
            params.height = view.measuredHeight
        }
        addMoveEvent()
        windowManager.addView(rootView, params)
    }

    fun close(){
        windowManager.removeView(rootView)
    }

    private fun addMoveEvent(){
        var lastX = 0
        var lastY = 0
        //移动悬浮窗
        rootView?.setOnTouchListener{
                view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = 0
                    lastY = 0
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = motionEvent.rawX.toInt()
                    val nowY = motionEvent.rawY.toInt()
                    if (lastX != 0 && lastY != 0){
                        params?.apply {
                            x += nowX - lastX
                            y += nowY - lastY
                        }
                        windowManager.updateViewLayout(rootView, params)
                    }
                    lastX = nowX
                    lastY = nowY
                }
            }

            false
        }
    }
}