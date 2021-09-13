package com.gfz.lab.utils

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.gfz.lab.app.KTApp
import com.gfz.lab.ext.toPX

/**
 * 管理小窗口布局
 * created by gaofengze on 2021/2/22
 */
class OverlayUtil {
    //要引用的布局文件.
    private var rootView: View? = null
    private var lastX = 0
    private var lastY = 0
    private var width = 180.toPX()
    private var height = 120.toPX()

    //布局参数.
    private val params: WindowManager.LayoutParams = WindowManager.LayoutParams()

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
        params.gravity = Gravity.START or Gravity.TOP
        params.width = 180.toPX()
        params.height = 120.toPX()

    }

    fun showWindow(view: View){
        rootView = view
        lastX = 0
        lastY = 0
        windowManager.addView(rootView, params)
    }

    fun close(){
        windowManager.removeView(rootView)
    }

    fun handlerMoveEvent(motionEvent: MotionEvent){
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = 0
                lastY = 0
            }
            MotionEvent.ACTION_MOVE -> {
                val nowX = motionEvent.rawX.toInt()
                val nowY = motionEvent.rawY.toInt()
                if (lastX != 0 && lastY != 0){
                    var x = params.x + nowX - lastX
                    var y = params.y + nowY - lastY
                    // 处理超出屏幕
                    val maxX = ScreenUtil.getScreenWidth() - width
                    val maxY = ScreenUtil.getScreenHeight() - height
                    if (x < 0){
                        x = 0
                    }else if(x > maxX){
                        x = maxX
                    }
                    if (y < 0){
                        y = 0
                    }else if(y > maxY){
                        y = maxY
                    }

                    params.x = x
                    params.y = y
                    windowManager.updateViewLayout(rootView, params)
                }
                lastX = nowX
                lastY = nowY
            }
        }
    }
}