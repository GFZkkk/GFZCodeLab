package com.gfz.common.utils

import android.widget.Toast
import com.gfz.common.base.BaseApplication

object ToastUtil {

    private val toast: Toast by lazy {
        Toast(BaseApplication.appContext)
    }

    fun showToast(text: CharSequence) {
        Toast.makeText(
            BaseApplication.appContext,
            text,
            if (text.length > 10) {
                Toast.LENGTH_LONG
            } else {
                Toast.LENGTH_SHORT
            }
        ).show()
    }
}