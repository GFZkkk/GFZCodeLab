package com.gfz.lab.viewmodel

import androidx.lifecycle.MutableLiveData
import com.gfz.ui.base.ext.asLiveData
import com.gfz.common.utils.RandomUtil
import com.gfz.common.utils.TopLog
import com.gfz.ui.base.page.BaseViewModel
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

    fun getData() {
        _dataList.value = listOf(
            "112332131", "122112", "122112", "11", "123", "12233", "112332131"
        )
    }

    fun start() {
        val job = startJob(true) {
            autoCancel()
        }
    }

    fun reStart() {
        reStartSingleJob() {
            task()
        }
    }

    fun startSingle() {
        startSingleJob() {
            task()
        }
    }

    fun stop() {
        stopSingleJob()
    }

    private suspend fun task() {
        while (true) {
            delay(1000)
            val list = RandomUtil.getRandomList(
                testList,
                RandomUtil.getRandomIndex(testList.size - 1)
            )
            _dataList.value = list
        }
    }

    private suspend fun autoCancel() {
        var i = 10
        while (i-- > 0) {
            delay(1500)
            val list = RandomUtil.getRandomList(
                testList,
                RandomUtil.getRandomIndex(testList.size - 1)
            )
            _dataList.value = list
        }
    }

}