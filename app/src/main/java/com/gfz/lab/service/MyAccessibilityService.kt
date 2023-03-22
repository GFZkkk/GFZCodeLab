package com.gfz.lab.service

import android.accessibilityservice.AccessibilityService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.accessibility.AccessibilityEvent

/**
 *
 * created by xueya on 2023/3/20
 */
class MyAccessibilityService: AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }
}