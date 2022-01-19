package com.gfz.lab.ext

import android.content.Context
import com.gfz.common.ext.getCompatColor
import com.gfz.lab.app.KTApp
import com.gfz.lab.ui.base.BaseFragment

fun BaseFragment.getColor(resId: Int) = context?.getCompatColor(resId)