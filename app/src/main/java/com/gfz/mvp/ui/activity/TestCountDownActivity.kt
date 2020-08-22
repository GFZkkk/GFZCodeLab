package com.gfz.mvp.ui.activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.gfz.mvp.R
import com.gfz.mvp.base.BaseActivity
import com.gfz.mvp.service.DrawOverService
import kotlinx.android.synthetic.main.activity_countdown.*

/**
 * created by gfz on 2020/5/5
 **/
class TestCountDownActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_countdown

    override fun initView() {

    }

    override fun initData() {
        switch_check.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                openService()
            }else{
                stopService(Intent(this, DrawOverService::class.java))
            }
        }
    }

    fun openService(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(this)) {
                val intent = Intent(this, DrawOverService::class.java)
                Toast.makeText(this,"已开启Toucher",Toast.LENGTH_SHORT).show()
                startService(intent)
            } else {
                //若没有权限，提示获取.
                val intent =  Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                Toast.makeText(this,"需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
        } else {
            //SDK在23以下，不用管.
            val intent = Intent(this, DrawOverService::class.java)
            startService(intent)
        }
    }

}