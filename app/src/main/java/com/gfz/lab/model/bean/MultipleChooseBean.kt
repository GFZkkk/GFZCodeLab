package com.gfz.lab.model.bean

/**
 * created by gaofengze on 2020/4/14
 */

data class MultipleChooseBean(val title: String, val groupId: Int = 0): com.gfz.recyclerview.bean.BaseMultipleChooseBean(){
    override fun getBaseGroupId(): Int {
        return groupId
    }
}