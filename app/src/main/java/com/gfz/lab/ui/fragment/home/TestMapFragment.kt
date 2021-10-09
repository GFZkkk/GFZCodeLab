package com.gfz.lab.ui.fragment.home

import android.content.Context
import android.os.Looper
import android.view.MotionEvent
import android.view.ViewTreeObserver
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.lifecycle.LifecycleObserver
import com.gfz.lab.databinding.FragmentMapBinding
import com.gfz.lab.ext.round
import com.gfz.lab.ext.toPX
import com.gfz.lab.ui.base.BaseVBFragment
import com.gfz.lab.utils.ScreenUtil
import com.gfz.lab.utils.TopLog
import com.gfz.lab.utils.task.TimeLoop
import kotlin.math.max
import kotlin.math.min

/**
 *
 * created by gaofengze on 2021/10/8
 */
class TestMapFragment : BaseVBFragment<FragmentMapBinding>() {

    private val frame = 33

    var x = 0
    var y = 0

    var touchX = 0
    var touchY = 0

    private var controlSize = arrayOf(0,0)
    var controlPadding = 3.toPX()

    private var pointSize = arrayOf(0,0)

    var pointX = 0f
    var pointY = 0f

    override fun initView() {
        // 初始化事件
        initEvent()
        // 测量
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
        // 准备
        val timeLoop = TimeLoop(handler, { execute() }, frame)
        // 准备好之后开始
        Looper.myQueue().addIdleHandler {
            timeLoop.run()
            false
        }
    }

    private val layoutListener: ViewTreeObserver.OnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        measuredSize()
    }

    private fun initEvent(){
        binding.root.setOnTouchListener { v, event ->
            x = event.x.toInt()
            y = event.y.toInt()
            false
        }

        binding.flControl.setOnTouchListener { v, event ->
            touchX = event.x.toInt()
            touchY = event.y.toInt()
            when(event.action){
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    touchX = event.x.toInt()
                    touchY = event.y.toInt()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    resetTouchPosition()
                }
            }
            false
        }
    }

    private fun measuredSize(){
        controlSize[0] = binding.flControl.width
        controlSize[1] = binding.flControl.height

        pointSize[0] = binding.point.width
        pointSize[1] = binding.point.height

        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)

        resetTouchPosition()

    }

    private fun execute(){
        context?.apply {
            // 设置控制小点的位置
            setPointPositionByTouch()

            printLog(this)
        }

    }

    private fun printLog(context: Context){
        val info = """宽:${ScreenUtil.getScreenWidth(context)}, 高:${ScreenUtil.getScreenHeight(context)}
                |位置：{${x},${y}}
                |触摸：{${touchX},${touchY}}
                |面板：{${controlSize[0]},${controlSize[1]}}
                |小球：{${pointSize[0]},${pointSize[1]}}
                |控制：{${pointX},${pointX}}""".trimMargin()
        binding.tvInfo.text = info
    }

    private fun setPointPositionByTouch(){

        pointX = touchX.round(controlPadding, controlSize[0] - pointSize[0] - controlPadding).toFloat()
        pointY = touchY.round(controlPadding, controlSize[1] - pointSize[1] - controlPadding).toFloat()

        binding.point.x = pointX
        binding.point.y = pointY
    }

    private fun resetTouchPosition(){
        touchX = (controlSize[0] shr 1) - (pointSize[0] shr 1)
        touchY = (controlSize[1] shr 1) - (pointSize[1] shr 1)
    }


}