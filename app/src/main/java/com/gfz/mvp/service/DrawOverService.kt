package com.gfz.mvp.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gfz.mvp.R
import com.gfz.mvp.utils.TopLog
import com.gfz.mvp.utils.toPX
import java.text.SimpleDateFormat
import java.util.*


/**
 * created by gfz on 2020/5/5
 **/
class DrawOverService : Service() {
    //要引用的布局文件.
    private var countDownLayout: ConstraintLayout? = null

    //布局参数.
    private var params: WindowManager.LayoutParams? = null

    //实例化的WindowManager.
    private var windowManager: WindowManager? = null

    //状态栏高度.（接下来会用到）
    private var statusBarHeight = -1

    private val width = 180

    private val height = 120

    private val handler = Handler()

    private var timeText : TextView? = null
    private var ivClose : ImageView? = null
    private var bg : View? = null

    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.CHINESE)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initView()
        initData()
        initEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTime)
    }

    private fun initData(){
        timeText?.text = simpleDateFormat.format(Date())
        ivClose?.setImageResource(R.drawable.close_white)
    }

    private fun initView(){
        //赋值WindowManager&LayoutParam.
        params = WindowManager.LayoutParams()
        windowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params?.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }else{
            params?.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
        //设置效果为背景透明.
        params?.format = PixelFormat.RGBA_8888
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params?.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

        //设置窗口初始停靠位置.
        params?.gravity = Gravity.LEFT or Gravity.TOP
        params?.x = 0
        params?.y = 0

        //设置悬浮窗口长宽数据.
        params?.width = width.toPX()
        params?.height = height.toPX()

        val inflater = LayoutInflater.from(application)
        //获取浮动窗口视图所在布局.
        countDownLayout = inflater.inflate(R.layout.layout_count_down, null) as ConstraintLayout
        //添加countDownLayout
        windowManager?.addView(countDownLayout, params)

        //主动计算出当前View的宽高信息.
        countDownLayout?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        //用于检测状态栏高度.
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        TopLog.i( "状态栏高度为:$statusBarHeight")

        timeText = countDownLayout?.findViewById(R.id.tv_time)
        ivClose = countDownLayout?.findViewById(R.id.iv_close)
        bg = countDownLayout?.findViewById(R.id.v_bg)
    }

    private fun initEvent(){
        //移动悬浮窗
        bg?.setOnTouchListener{
                view, motionEvent ->
            params?.x = motionEvent.rawX.toInt() - width.toPX() / 2
            params?.y = motionEvent.rawY.toInt() - height.toPX() / 2 - statusBarHeight
            windowManager?.updateViewLayout(countDownLayout,params)
            false
        }
        ivClose?.setOnClickListener {
            stopSelf()
        }
        handler.post(updateTime)

    }

    private val updateTime = object : Runnable {
        override fun run() {
            timeText?.text = simpleDateFormat.format(Date())
            handler.postDelayed(this,100)
        }
    }
}