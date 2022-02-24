package com.gfz.lab.ext

import com.gfz.common.ext.getCompatColor
import com.gfz.ui.base.page.BaseFragment

fun BaseFragment.getColor(resId: Int) = context?.getCompatColor(resId)