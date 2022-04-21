package com.gfz.lab.model.bean

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes

/**
 *
 * created by gaofengze on 2021/5/13
 */
data class TabItemBean(
    @DrawableRes val activeIconRes: Int,
    @DrawableRes val defaultIconRes: Int,
    @StringRes val desRes: Int,
    @ColorRes val textActiveColorRes: Int,
    @ColorRes val textDefaultColorRes: Int,
){
    fun getIconRes(choose: Boolean) = if (choose) { activeIconRes } else { defaultIconRes }

    fun getDesColorRes(choose: Boolean) = if (choose) { textActiveColorRes } else { textDefaultColorRes }
}
