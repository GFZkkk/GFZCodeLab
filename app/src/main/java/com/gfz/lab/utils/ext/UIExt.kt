package com.gfz.lab.utils.ext

import androidx.fragment.app.Fragment
import com.gfz.common.ext.getCompatColor

fun Fragment.getColor(resId: Int) = requireContext().getCompatColor(resId)