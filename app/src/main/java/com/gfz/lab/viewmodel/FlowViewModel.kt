package com.gfz.lab.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gfz.common.ext.asLiveData
import com.gfz.common.ext.launchSafe
import com.gfz.common.task.JobItem
import com.gfz.common.utils.RandomUtil
import com.gfz.common.utils.TopLog
import com.gfz.lab.base.BaseViewModel
import kotlinx.coroutines.delay

/**
 * Created by xueya on 2022/1/27
 */
class FlowViewModel : BaseViewModel() {
    private val _dataList: MutableLiveData<List<String>> = MutableLiveData()
    val dataList = _dataList.asLiveData()
    private val testList = listOf(
        "123",
        "123456",
        "12233",
        "11",
        "112332131",
        "122112",
        "1312",
        "12233",
        "11",
        "112332131",
        "122112",
        "122112",
        "1312",
        "12233",
        "12233",
        "11",
        "112332131",
        "122112"
    )
    fun getData(){
        startSingleJob(true){
            task()
        }
    }

    fun start(){
        startJob(true){
            task()
        }
    }

    fun reStart(){
        reStartSingleJob(true){
            task()
        }
    }

    fun startSingle(){
        startSingleJob(true){
            task()
        }
    }

    fun stop(){
        stopJob()
    }

    private suspend fun task(){
        while (true){
            delay(1500)
            val list = RandomUtil.getRandomList(
                testList,
                RandomUtil.getRandomIndex(testList.size - 1)
            )
            TopLog.e(list.toString())
            _dataList.value = list
        }
    }

}