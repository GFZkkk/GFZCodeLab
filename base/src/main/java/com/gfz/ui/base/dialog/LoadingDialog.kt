package com.gfz.ui.base.dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.gfz.ui.base.R

/**
 *
 * created by xueya on 2022/3/17
 */
class LoadingDialog() : DialogFragment(R.layout.layout_loading) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, 0)
    }
}