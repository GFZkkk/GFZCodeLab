package com.gfz.mvp.utils

import android.widget.Toast
import androidx.annotation.StringRes
import com.gfz.mvp.data.KTApp

object ToastUtil{

    private val toast: Toast by lazy {
        Toast(KTApp.appContext)
    }

    fun showToast(text: CharSequence){
        toast.cancel()
        toast.setText(text)
        if (text.length > 10){
            toast.duration = Toast.LENGTH_LONG
        }else{
            toast.duration = Toast.LENGTH_SHORT
        }
        toast.show()
    }
}