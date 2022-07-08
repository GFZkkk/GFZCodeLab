package com.gfz.common.loop

import com.gfz.common.utils.RecyclerPool

/**
 *
 * created by xueya on 2022/7/8
 */
class TimeLoopGroup {
    private val pool by lazy {
        RecyclerPool()
    }
}