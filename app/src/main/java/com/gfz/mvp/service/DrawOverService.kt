package com.gfz.mvp.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.view.*
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.view.WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gfz.mvp.R
import com.gfz.mvp.utils.*
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

    private var sum = 0
    private var key = 100
    private var start = false

    private var timeText : TextView? = null
    private var tvSum : TextView? = null
    private var tvStart : TextView? = null
    private var tvBoth : TextView? = null
    private var tvFirst : TextView? = null
    private var tvSecond : TextView? = null
    private var ivClose : ImageView? = null
    private var bg : ConstraintLayout? = null

    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss:SS", Locale.CHINESE)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initView()
        countDownLayout?.post {
            initData()
            initEvent()
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(updateTime)
        countDownLayout.apply {
            windowManager?.removeView(this)
        }

        super.onDestroy()

    }

    private fun initData(){
        TopLog.e("初始化")
        stop()

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
//        params?.format = PixelFormat.RGBA_8888
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params?.flags = FLAG_NOT_FOCUSABLE or FLAG_WATCH_OUTSIDE_TOUCH
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

    }

    private fun initEvent(){
        //绑定控件
        timeText = countDownLayout?.findViewById(R.id.tv_time)
        tvSum = countDownLayout?.findViewById(R.id.tv_sum)
        tvStart = countDownLayout?.findViewById(R.id.tv_start)
        tvBoth = countDownLayout?.findViewById(R.id.tv_both)
        tvFirst = countDownLayout?.findViewById(R.id.tv_first)
        tvSecond = countDownLayout?.findViewById(R.id.tv_second)
        ivClose = countDownLayout?.findViewById(R.id.iv_close)
        bg = countDownLayout?.findViewById(R.id.cl_body)
        //移动悬浮窗
        bg?.setOnTouchListener{
                view, motionEvent ->
            TopLog.e(motionEvent.action)
            when (motionEvent.action) {

                MotionEvent.ACTION_MOVE -> {
                    params?.x = motionEvent.rawX.toInt() - width.toPX() / 2
                    params?.y = motionEvent.rawY.toInt() - height.toPX() / 2 - statusBarHeight
                    windowManager?.updateViewLayout(countDownLayout,params)
                }
            }

            false
        }
        ivClose?.setOnClickListener {
            TopLog.e("停止服务")
            stopSelf()
        }
        tvStart?.setOnClickListener {
            if (start){
                stop()
            }else{
                key = 100
                start()
            }
        }
        tvBoth?.setOnClickListener {
            if (!start){
                key = 100
                start()
            }
        }
        tvFirst?.setOnClickListener {
            if (!start){
                key = 95
                start()
            }
        }
        tvSecond?.setOnClickListener {
            if (!start){
                key = 105
                start()
            }
        }
        handler.post(updateTime)

    }

    private val updateTime = object : Runnable {
        override fun run() {
            handler.postDelayed(this,100)
            timeText?.text = simpleDateFormat.format(Date())
            if (start){
                sum++
                tvSum?.text = String.format("%.1f", sum / 10.0f)
                if (sum == key){
                    stop()
                }

            }

        }
    }

    private fun start(){
        tvStart?.text = "暂停"
        tvStart?.setTextColor(getmColor(R.color.colorAccent))
        start = true
        status(false)
    }

    private fun end(){
        sum = 0
        tvStart?.text = "开始"
        tvStart?.setTextColor(getmColor(R.color.colorPrimary))
        start = false
        status(false)
    }

    private fun stop(){
        sum = 0
        start = false
        status(true)
    }

    private fun status(start: Boolean){
        tvStart.setDisplay(!start)
        tvBoth.setDisplay(start)
        tvSecond.setDisplay(start)
        tvFirst.setDisplay(start)
    }
}