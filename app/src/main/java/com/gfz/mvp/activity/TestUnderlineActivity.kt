package com.gfz.mvp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gfz.mvp.databinding.ActivityCountdownBinding
import com.gfz.mvp.databinding.ActivityTestUnderlineBinding
import com.gfz.mvp.utils.viewBind

class TestUnderlineActivity : AppCompatActivity() {
    private val binding: ActivityTestUnderlineBinding by viewBind()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding){
            suv.text = "punch"
        }
    }
}