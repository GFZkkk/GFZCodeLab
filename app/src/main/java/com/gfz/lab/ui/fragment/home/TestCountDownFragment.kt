package com.gfz.lab.ui.fragment.home

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import com.gfz.lab.databinding.FragmentCountdownBinding
import com.gfz.lab.service.DrawOverService
import com.gfz.lab.ui.base.BaseVBFragment
import com.gfz.lab.utils.ToastUtil

/**
 * created by gfz on 2020/5/5
 **/
class TestCountDownFragment : BaseVBFragment<FragmentCountdownBinding>() {

    override fun initView() {
        binding.switchCheck.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (!binding.switchCheck.isChecked) {
                    openService()
                } else {
                    binding.switchCheck.isChecked = false
                    activity?.stopService(Intent(activity, DrawOverService::class.java))
                }
                true
            } else {
                false
            }

        }
    }

    override fun getTitleText(): String {
        return "悬浮计时试验区"
    }

    private fun openService() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(requireContext())) {
                startService()
            } else {
                //若没有权限，提示获取.
                ToastUtil.showToast("需要取得权限以使用悬浮窗")
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                // packageName 是应用的包名
                intent.data = Uri.parse("package:${activity?.packageName}")
                // 直接打开系统的同意界面给用户操作
                startActivity(intent)
            }
        } else {
            //SDK在23以下，不用管.
            startService()
        }
    }

    private fun startService() {
        binding.switchCheck.isChecked = true
        activity?.startService(Intent(activity, DrawOverService::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if (requestCode == 1000) {
            if (Settings.canDrawOverlays(this)) {
                // 用户同意了就可以进行其它操作了
                startService()
            }
        }*/
    }

}