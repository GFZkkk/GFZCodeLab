package com.gfz.lab.activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.MotionEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.gfz.lab.base.BaseActivity
import com.gfz.lab.databinding.ActivityCountdownBinding
import com.gfz.lab.service.DrawOverService
import com.gfz.lab.utils.viewBind

/**
 * created by gfz on 2020/5/5
 **/
class TestCountDownActivity: BaseActivity() {

    private val binding: ActivityCountdownBinding by viewBind()

    override fun initData() {
        binding.switchCheck.setOnTouchListener{ view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP){
                if (!binding.switchCheck.isChecked){
                    openService()
                }else{
                    binding.switchCheck.isChecked = false
                    stopService(Intent(this, DrawOverService::class.java))
                }
                true
            }else{
                false
            }

        }
    }

    private fun openService(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(this)) {
                startService()
            } else {
                //若没有权限，提示获取.
                Toast.makeText(this,"需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                // packageName 是应用的包名
                intent.data = Uri.parse("package:$packageName")
                // 直接打开系统的同意界面给用户操作
                startActivityForResult(intent, 1000)
            }
        } else {
            //SDK在23以下，不用管.
            startService()
        }
    }

    private fun startService(){
        binding.switchCheck.isChecked = true
        startService(Intent(this, DrawOverService::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            if (Settings.canDrawOverlays(this)) {
                // 用户同意了就可以进行其它操作了
                startService()
            }
        }
    }

}