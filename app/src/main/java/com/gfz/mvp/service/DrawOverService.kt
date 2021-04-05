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
import androidx.viewbinding.ViewBinding
import com.gfz.mvp.R
import com.gfz.mvp.databinding.LayoutCountDownBinding
import com.gfz.mvp.module.OverlayManager
import com.gfz.mvp.utils.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * created by gfz on 2020/5/5
 **/
class DrawOverService : Service() {

    //状态栏高度.（接下来会用到）
    private var statusBarHeight = -1

    private val handler = Handler()

    private var sum = 0
    private var start = false
    private var isReady = false

    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss:SS", Locale.CHINESE)

    private val binding: LayoutCountDownBinding = viewBind()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        OverlayManager.showWindow(binding.root)
        reset()
        initEvent()
        handler.post(updateTime)
    }

    override fun onDestroy() {
        handler.removeCallbacks(updateTime)
        OverlayManager.close()
        super.onDestroy()
    }

    private fun initEvent(){
        binding.apply {
            //移动悬浮窗
            clBody.setOnTouchListener{
                    view, motionEvent ->
                when (motionEvent.action) {
                    //点击屏幕外区域
                    MotionEvent.ACTION_OUTSIDE -> {
                        if (isReady){
                            start()
                        }
                    }
                }

                false
            }
            ivClose.setOnClickListener {
                TopLog.e("停止服务")
                stopSelf()
            }
            tvStart.setOnClickListener {
                if (start){
                    reset()
                }else{
                    sum = 100
                    start()
                }
            }
            tvBoth.setOnClickListener {
                if (!start){
                    sum = 100
                    resume()
                }
            }
            tvFirst.setOnClickListener {
                if (!start){
                    sum = 95
                    resume()
                }
            }
            tvSecond.setOnClickListener {
                if (!start){
                    sum = 105
                    resume()
                }
            }
        }
    }

    private val updateTime = object : Runnable {
        override fun run() {
            handler.postDelayed(this,100)
            binding.tvTime.text = simpleDateFormat.format(Date())
            if (start && sum > 0){
                sum--
                binding.tvSum.text = String.format("%.1f", sum / 10.0f)
                if (sum == 0){
                    reset()
                }

            }

        }
    }

    private fun start(){
        binding.tvStart.text = "暂停"
        binding.tvStart.setTextColor(getCompatColor(R.color.colorAccent))
        start = true
        isReady = false
        status(false)
    }

    private fun resume(){
        binding.tvStart.text = "准备中"
        binding.tvStart.setTextColor(getCompatColor(R.color.colorPrimary))
        start = false
        isReady = true
        status(false)
    }

    private fun reset(){
        start = false
        isReady = false
        status(true)
    }

    private fun status(start: Boolean){
        binding.apply {
            tvStart.setDisplay(!start)
            tvBoth.setDisplay(start)
            tvSecond.setDisplay(start)
            tvFirst.setDisplay(start)
        }

    }
}