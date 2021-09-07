package com.gfz.mvp.model.bean

/**
 * created by gaofengze on 2020/4/14
 */

data class MultipleChooseBean(val title: String, val groupId: Int = 0): BaseMultipleChooseBean(){
    override fun getBaseGroupId(): Int {
        return groupId
    }
}