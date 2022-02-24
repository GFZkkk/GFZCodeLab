package com.gfz.lab.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.*
import com.gfz.common.utils.TopLog
import com.gfz.lab.R
import com.gfz.lab.databinding.LayoutCountDownBinding
import com.gfz.common.ext.getCompatColor
import com.gfz.common.ext.setDisplay
import com.gfz.ui.base.util.viewBind
import com.gfz.lab.utils.OverlayUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * created by gfz on 2020/5/5
 **/
class DrawOverService : Service() {

    private var handler: Handler? = null

    private var sum = 0
    private var start = false
    private var isReady = false

    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss:SS", Locale.CHINESE)

    private val binding: LayoutCountDownBinding by viewBind()

    private val overlayManager: OverlayUtil = OverlayUtil()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        overlayManager.showWindow(binding.root)
        reset()
        initEvent()
        handler = Looper.myLooper()?.let { Handler(it) }
        handler?.post(updateTime)
    }

    override fun onDestroy() {
        handler?.removeCallbacks(updateTime)
        overlayManager.close()
        super.onDestroy()
    }

    private fun initEvent(){
        with(binding) {
            //移动悬浮窗
            clBody.setOnTouchListener{
                    _, motionEvent ->
                overlayManager.handlerMoveEvent(motionEvent)
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
            handler?.postDelayed(this,100)
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
        with(binding) {
            tvStart.setDisplay(!start)
            tvBoth.setDisplay(start)
            tvSecond.setDisplay(start)
            tvFirst.setDisplay(start)
        }

    }
}