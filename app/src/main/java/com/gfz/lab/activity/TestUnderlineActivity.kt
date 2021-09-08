package com.gfz.lab.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gfz.lab.databinding.ActivityTestUnderlineBinding
import com.gfz.lab.utils.viewBind

class TestUnderlineActivity : AppCompatActivity() {
    private val binding: ActivityTestUnderlineBinding by viewBind()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding){
            suv.text = "punch"
        }
    }
}