package com.gfz.lab.utils

import android.widget.Toast
import com.gfz.lab.app.KTApp

object ToastUtil {

    private val toast: Toast by lazy {
        Toast(KTApp.appContext)
    }

    fun showToast(text: CharSequence) {
        Toast.makeText(
            KTApp.appContext,
            text,
            if (text.length > 10) {
                Toast.LENGTH_LONG
            } else {
                Toast.LENGTH_SHORT
            }
        ).show()
    }
}