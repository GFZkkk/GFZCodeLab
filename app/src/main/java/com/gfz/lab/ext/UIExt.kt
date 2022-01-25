package com.gfz.lab.ext

import com.gfz.common.ext.getCompatColor
import com.gfz.lab.ui.base.BaseFragment

fun BaseFragment.getColor(resId: Int) = context?.getCompatColor(resId)